package com.forkexec.pts.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import javax.xml.ws.BindingProvider;
import com.forkexec.pts.ws.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;
import javax.xml.ws.Response;
import java.util.concurrent.ExecutionException;
import javax.xml.ws.AsyncHandler;
import java.util.concurrent.Future;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

public class PointsFrontEnd {

	private List<Response<ReadResponse>> responseList = new LinkedList<Response<ReadResponse>>(); 	 	  //lista de mensagens read enviadas
	private List<Response<ReadResponse>> validResponseList = new LinkedList<Response<ReadResponse>>();	  //lista de respostas read válidas recebidas

	private List<Response<WriteResponse>> responseList2 = new LinkedList<Response<WriteResponse>>();      //lista de mensagens write enviadas
	private List<Response<WriteResponse>> validResponseList2 = new LinkedList<Response<WriteResponse>>(); //lista de respostas write válidas recebidas
	
	private List<PointsCliServer> pointsServers = new LinkedList<PointsCliServer>();
	private int[] weights;
	private int wt, rt, update = 0;
	private final UDDINaming uddiNaming;

	public PointsFrontEnd(final UDDINaming uddiNaming) {
		this.uddiNaming = uddiNaming;
		getServers();
	}

	private void getServers() {
		try {
			this.pointsServers.clear();
			Collection<UDDIRecord> ptsServers = this.uddiNaming.listRecords("A18_Points%");

			int size = ptsServers.size();
			this.weights = new int[size];

			for (UDDIRecord pts : ptsServers)
				this.pointsServers.add(new PointsCliServer(pts.getUrl(), pts.getOrgName()));

			int total = 0;
			for (int i = 0; i < size; i++) {
				// The first half has weight 2, the latter one has weight 1

				if (i < size / 2) {
					this.weights[i] = 5;
					total += 5;
				} else {
					this.weights[i] = 4;
					total += 4;
				}
			}
			// Read and Write Threshold
			rt = total / 2;
			wt = total - rt + 1;
			// wt >= rt+1
		} catch (UDDINamingException | PointsClientException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public int readAsync(String email) {
		
		if (this.update == 100) {
			getServers();
			this.update = 0;
		}
		else
			this.update++;
		
		List<Integer> resposta = null;
		int tag, availableServers = 0, maxTag = 0, maxVal = 0;
		responseList.clear();
		validResponseList.clear();
		
		// (1) envia read() para todos os gestores de réplica
		for (PointsCliServer pts : this.pointsServers) {
			this.responseList.add(pts.read(email)); 	// lista com todas as mensagens enviadas
		}

		// (2) aguardar que o peso total dos gestores de réplica que enviam uma
		//     resposta válida seja igual ou superior a read threshold (rt)
		while (availableServers < this.rt) {
			int servidorIndex = 0;
			Iterator<Response<ReadResponse>> itr = this.responseList.iterator();
			while (itr.hasNext()) {
				Response<ReadResponse> response = itr.next();
				if (response.isDone()) {
					availableServers += weights[servidorIndex];
					this.validResponseList.add(response);
					itr.remove();
				}
				servidorIndex += 1;
			}
		}

		// (3) selecionar a resposta recebida que tiver a tag mais alta
		for (Response<ReadResponse> response : this.validResponseList) {
			try {
				resposta = response.get().getReturn();
				tag = resposta.get(1);
				if (tag >= maxTag) {
					maxVal = resposta.get(0);
					maxTag = tag;
				}
			} catch (ExecutionException e) {
				System.out.println("Caught execution exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			} catch (InterruptedException e) {
				System.out.println("Caught execution exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}
		// (4) retornar maxVal (valor recebido com maior tag)
		return maxVal;
	}

	public String writeAsync(String email, int value) {     // throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception
		int availableServers = 0, maxTag = 0, tag;
		responseList.clear();
		validResponseList.clear();
		responseList2.clear();
		validResponseList2.clear();

		// (1) envia read() para todos os gestores de réplica
		for (PointsCliServer pts : this.pointsServers) {
			this.responseList.add(pts.read(email)); 	// lista com todas as mensagens enviadas
		}

		// (2) aguardar que o peso total dos gestores de réplica que enviam uma
		//     resposta válida seja igual ou superior a read threshold (rt)
		while (availableServers < this.rt) {
			int servidorIndex = 0;
			Iterator<Response<ReadResponse>> itr = this.responseList.iterator();
			while (itr.hasNext()) {
				Response<ReadResponse> response = itr.next();
				if (response.isDone()) {
					availableServers += this.weights[servidorIndex];
					this.validResponseList.add(response);
					itr.remove();
					try {
						tag = response.get().getReturn().get(1);
						if(tag > maxTag)
							maxTag = tag;
					} catch (ExecutionException e) {
						System.out.println("Caught execution exception.");
						System.out.print("Cause: ");
						System.out.println(e.getCause());
					} catch (InterruptedException e) {
						System.out.println("Caught execution exception.");
						System.out.print("Cause: ");
						System.out.println(e.getCause());
					}
				}
				servidorIndex += 1;
			}
		}
		availableServers = 0;

		// (3) criar tag a enviar
		int newTag = maxTag + 1;

		// (4) enviar write(email, val, newTag) a todos os gestores de réplica
		for (PointsCliServer pts : this.pointsServers) {
			this.responseList2.add(pts.write(email, value, newTag));
		}
		
		// (5) aguardar que o peso total dos gestores de réplica que enviam uma
		//     resposta válida seja igual ou superior a write threshold (wt)
		while (availableServers < this.wt) {
			//System.out.println("!! " + availableServers + " - " + this.wt);
			
			int servidorIndex = 0;
			Iterator<Response<WriteResponse>> itr = responseList2.iterator();
			//
			while (itr.hasNext()) {
				Response<WriteResponse> response = itr.next();
				if (response.isDone()) {
					//System.out.println("---> " + this.weights[servidorIndex] + " <-- ");
					// int jr = weights[servidorIndex];
					availableServers += this.weights[servidorIndex];
					this.validResponseList2.add(response);
					itr.remove();
					//System.out.println(availableServers + "<");
					break;
				}
				servidorIndex++;
			}
		}
		// (6) retorna ao cliente	
		return "ACK";
	}

	public void activateUser(String email) throws EmailAlreadyExistsFault_Exception {
		for (PointsCliServer pts : this.pointsServers) {
			pts.activateUser(email);
		}
	}
	
	public void sleep(int seconds) {
		try {
			System.out.println("--- Tolerância a Faltas: Por favor, termine um servidor nos próximos " + seconds + " segundos");
			Thread.sleep(seconds*1000);
			System.out.println("--- A testar sem um dos servidores:");	
		}
		catch(InterruptedException e) {
            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Caught exception: %s%n", e);
		}
	}

    public void ctrlClear() {
        for (PointsCliServer pts : this.pointsServers) {
			pts.ctrlClear();
		}
        this.responseList.clear();
        this.responseList2.clear();
        this.validResponseList.clear();
        this.validResponseList2.clear();
        this.update = 0;
        getServers();
    }

    public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
        for (PointsCliServer pts : this.pointsServers) {
			pts.ctrlInit(startPoints);
		}
	}
	
	public String ctrlPing(String inputMessage) {
		String retorno = "";
		for (PointsCliServer pts : this.pointsServers) {
			retorno = pts.ctrlPing(inputMessage);
			if(retorno != "")
				break;
		}
		return retorno;
	}
}