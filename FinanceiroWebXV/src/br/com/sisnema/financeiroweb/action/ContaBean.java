package br.com.sisnema.financeiroweb.action;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.negocio.ContaRN;
import br.com.sisnema.financeiroweb.util.ContextoUtil;
import br.com.sisnema.financeiroweb.util.RNException;

@ManagedBean
@RequestScoped
public class ContaBean extends ActionBean {
	
	private Conta selecionada = new Conta();
	private List<Conta> lista = null;

	public void salvar() {
		try {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			selecionada.setUsuario(contextoBean.getUsuarioLogado());
			
			ContaRN contaRN = new ContaRN();
			contaRN.salvar(selecionada);
			selecionada = new Conta();
			lista = null;
			
		} catch (RNException e) {
			apresentarMenssagemDeErro(e);
		}
	}
	
	public void excluir(){
		try {
			ContaRN contaRN = new ContaRN();
			contaRN.excluir(selecionada);
			selecionada = new Conta();
			lista = null;
		} catch (RNException e) {
			apresentarMenssagemDeErro(e);
		}
	}

	public List<Conta> getLista() {
		if(lista == null){
			ContaRN contaRN = new ContaRN();
			lista = contaRN.pesquisar(new Conta(ContextoUtil.getContextoBean().getUsuarioLogado()));
		}
		return lista;
	}
	
	public void tornarFavorita(){
		try {
			ContaRN contaRN = new ContaRN();
			contaRN.tornarFavorita(selecionada);
			selecionada = new Conta();
		} catch (RNException e) {
			apresentarMenssagemDeErro(e);
		}
	}
	
	

	public Conta getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Conta selecionada) {
		this.selecionada = selecionada;
	}

	public void setLista(List<Conta> lista) {
		this.lista = lista;
	}
}














