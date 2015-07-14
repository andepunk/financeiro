
package br.com.sisnema.financeiroweb.action;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.negocio.ChequeRN;
import br.com.sisnema.financeiroweb.util.ContextoUtil;
import br.com.sisnema.financeiroweb.util.MensagemUtil;
import br.com.sisnema.financeiroweb.util.RNException;

@ManagedBean(name="chequeBean")
@RequestScoped
public class ChequeBean extends ActionBean{

	private Cheque				selecionado	= new Cheque();
	private List<Cheque>	lista				= null;
	private Integer				chequeInicial;
	private Integer				chequeFinal;

	public void salvar() throws RNException{
		FacesContext context = FacesContext.getCurrentInstance();
		ContextoBean contextoBean = ContextoUtil.getContextoBean();
		Conta conta = contextoBean.getContaAtiva();
		
		int totalCheques = 0;
		if (this.chequeInicial == null || this.chequeFinal == null) 
		{
			apresentarMenssagemDeErro(MensagemUtil.getMensagem("cheque_erro_sequencia"));
			
		} 
		else if (this.chequeFinal.intValue() < this.chequeInicial.intValue()) 
		{
			apresentarMenssagemDeErro(MensagemUtil.getMensagem("cheque_erro_sequencia", this.chequeInicial, this.chequeFinal));
			
		} 
		else 
		{
			ChequeRN chequeRN = new ChequeRN();
			
			try{
				totalCheques = chequeRN.salvarSequencia(conta, this.chequeInicial, this.chequeFinal);
			}catch(RNException e){
				apresentarMenssagemDeErro(MensagemUtil.getMensagem("Erro ao salvar cheque "+ e.getMessage()));
			}
			
			
			apresentarMenssagemDeSucesso(MensagemUtil.getMensagem("OK", this.chequeInicial, this.chequeFinal));
			
			
			
			this.lista = null;
		}
	}

	public void excluir() {
		ChequeRN chequeRN = new ChequeRN();
		try {
			chequeRN.excluir(this.selecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("cheque_erro_excluir");
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			context.addMessage(null, msg);
		}
		this.lista = null;
	}

	public void cancelar() {
		ChequeRN chequeRN = new ChequeRN();
		try {
			chequeRN.cancelarCheque(this.selecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("");
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			context.addMessage(null, msg);
			
			apresentarMenssagemDeErro(MensagemUtil.getMensagem("cheque_erro_cancelar"));
		}
		this.lista = null;
	}

	public List<Cheque> getLista() {
		if (this.lista == null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			Conta conta = contextoBean.getContaAtiva();
			
			Cheque c = new Cheque();
			c.setConta(conta);			

			ChequeRN chequeRN = new ChequeRN();
			
			this.lista = chequeRN.pesquisar(c);
		}
		return this.lista;
	}

	public Cheque getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Cheque selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}
}
