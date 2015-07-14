package br.com.sisnema.financeiroweb.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;

import br.com.sisnema.financeiroweb.domain.SituacaoCheque;
import br.com.sisnema.financeiroweb.model.Categoria;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.ChequeId;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Lancamento;
import br.com.sisnema.financeiroweb.negocio.ChequeRN;
import br.com.sisnema.financeiroweb.negocio.LancamentoRN;
import br.com.sisnema.financeiroweb.util.ContextoUtil;
import br.com.sisnema.financeiroweb.util.RNException;

@ManagedBean
@RequestScoped
public class LancamentoBean extends ActionBean {

	private List<Lancamento> lista;
	private List<Double> saldos = new ArrayList<Double>();
	private float saldoGeral;
	private Lancamento editado = new Lancamento();
	
	private List<Lancamento> listaAteHoje;
	private List<Lancamento> listaFuturos;
	
	private Integer numeroCheque;
	
	public LancamentoBean() {
		novo();
	}

	public void novo() {
		editado = new Lancamento();
		editado.setData(new Date());
		numeroCheque = null;
	}
	
	public void editar() {
		Cheque cheque = editado.getCheque();
		if (cheque != null) {
			numeroCheque = cheque.getId().getNnumero();
		}
	}
	
	public void salvar() {
		ContextoBean contextoBean = ContextoUtil.getContextoBean();
		editado.setUsuario(contextoBean.getUsuarioLogado());
		editado.setConta(contextoBean.getContaAtiva());

		// O lancamento pode possuir um cheque
		if (numeroCheque != null) {
			ChequeRN chequeRN = new ChequeRN();
			ChequeId chequeId = new ChequeId();
			chequeId.setConta(contextoBean.getContaAtiva().getCodigo());
			chequeId.setNnumero(numeroCheque);
			Cheque chequeAux = new Cheque();
			chequeAux.setId(chequeId);	
			Cheque cheque = null;
			cheque = chequeRN.obterPorId(chequeAux);
			
			// Antes de baixar o cheque, valida-se se o mesmo existe
			// e não esteja cancelado...
			if (cheque == null) {
				apresentarMenssagemDeErro("Cheque não cadastrado");
				return;
			} else if (SituacaoCheque.C.equals(cheque.getSituacao())) {
				apresentarMenssagemDeErro("Cheque já cancelado");
				return;
			} else {
				editado.setCheque(cheque);
				try {
					// baixar o cheque, ou seja, alterar sua situação para Baixado
					chequeRN.baixarCheque(chequeId, editado);
				} catch (RNException e) {
					apresentarMenssagemDeErro("Erro ao baixar cheque: " + e.getMessage());
					return;
				}
			}
		}
		
		LancamentoRN lrn = new LancamentoRN();
		try {
			lrn.salvar(editado);
		} catch (RNException e) {
			apresentarMenssagemDeErro(e);
			return;
		}
		
		novo();
		lista = null;
	}

	public void mudouCheque(ValueChangeEvent event) {
		Integer chequeAnterior = (Integer) event.getOldValue();
		if (chequeAnterior != null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			ChequeRN chequeRN = new ChequeRN();
			try {
				chequeRN.desvinculaLancamento(contextoBean.getContaAtiva(), chequeAnterior);
			} catch (RNException e) {
				apresentarMenssagemDeErro("Erro ao mudar cheque: " + e.getMessage());
				return;
			}
		}
	}
	
	public void excluir() {
		LancamentoRN lancamentoRN = new LancamentoRN();
		try {
			editado = lancamentoRN.obterPorId(editado);
			lancamentoRN.excluir(editado);				
			
		} catch (RNException e) {
			apresentarMenssagemDeErro(e);
			return;
		}
		lista = null;
	}

	public List<Lancamento> getLista() {
		if (lista == null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			Conta conta = contextoBean.getContaAtiva();
			
			Calendar dataSaldo = new GregorianCalendar();
			dataSaldo.add(Calendar.MONTH, -1);
			dataSaldo.add(Calendar.DAY_OF_MONTH, -1);
			
			
			// Pega-se uma instancia de calendar com a data atual
			Calendar inicio = new GregorianCalendar();
			
			//tirando um mes da data atual
			inicio.add(Calendar.MONTH, -1);
			
			LancamentoRN lancamentoRN = new LancamentoRN();
			saldoGeral = lancamentoRN.saldo(conta, dataSaldo.getTime());
			lista = lancamentoRN.pesquisar(conta, inicio.getTime(), null);
			
			Categoria categoria = null;
			double saldo = saldoGeral;
			for (Lancamento lancamento : lista) {
				categoria = lancamento.getCategoria();
				saldo += (lancamento.getValor().floatValue() * categoria.getFator());
				saldos.add(saldo);
			}			
		}	
		
		return lista;
	}
	
	public List<Lancamento> getListaAteHoje() {
		if (listaAteHoje == null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			Conta conta = contextoBean.getContaAtiva();
			
			LancamentoRN lancamentoRN = new LancamentoRN();
			listaAteHoje = lancamentoRN.pesquisar(conta, null, new Date());
			
		}
		
		return listaAteHoje;
	} 
	
	public List<Lancamento> getListaFuturos() {
		if (listaFuturos == null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean();
			Conta conta = contextoBean.getContaAtiva();
			
			Calendar amanha = new GregorianCalendar();
			amanha.add(Calendar.DAY_OF_MONTH, 1);
			
			LancamentoRN lancamentoRN = new LancamentoRN();
			listaFuturos = lancamentoRN.pesquisar(conta, amanha.getTime(), null);
		}
		
		return listaFuturos;
	}

	public Lancamento getEditado() {
		return editado;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public List<Double> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<Double> saldos) {
		this.saldos = saldos;
	}

	public float getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(float saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public Integer getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(Integer numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}

	public void setListaAteHoje(List<Lancamento> listaAteHoje) {
		this.listaAteHoje = listaAteHoje;
	}

	public void setListaFuturos(List<Lancamento> listaFuturos) {
		this.listaFuturos = listaFuturos;
	}
	
	
}
