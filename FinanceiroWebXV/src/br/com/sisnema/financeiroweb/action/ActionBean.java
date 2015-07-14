package br.com.sisnema.financeiroweb.action;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.sisnema.financeiroweb.util.RNException;

public abstract class ActionBean {
	
	protected final void apresentarMenssagemDeErro(RNException e){
		apresentarMenssagemDeErro(e.getMessage());
	}
	
	protected final void apresentarMenssagemDeErro(String msg){
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, msg, ""));
	}
	
	
	protected final void apresentarMenssagemDeSucesso(String msg){
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, msg, ""));
	}

}
