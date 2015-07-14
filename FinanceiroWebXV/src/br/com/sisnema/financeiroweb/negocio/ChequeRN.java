package br.com.sisnema.financeiroweb.negocio;

import java.util.Date;
import java.util.List;

import br.com.sisnema.financeiroweb.dao.ChequeDAO;
import br.com.sisnema.financeiroweb.domain.SituacaoCheque;
import br.com.sisnema.financeiroweb.model.Cheque;
import br.com.sisnema.financeiroweb.model.ChequeId;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Lancamento;
import br.com.sisnema.financeiroweb.util.DAOException;
import br.com.sisnema.financeiroweb.util.RNException;


public class ChequeRN extends RN<Cheque> {

	public ChequeRN() {
		super(new ChequeDAO());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void salvar(Cheque model) throws RNException {
		try {
			dao.salvar(model);
		}catch (DAOException e) {
			throw new RNException(e);
		}

	}

	@Override
	public void excluir(Cheque model) throws RNException {
		if(SituacaoCheque.N.equals(model.getSituacao())){
			try {
				dao.excluir(model);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			throw new RNException("Não é possivel excluir cheque, status não permitido para operação");
		}

	}

	@Override
	public Cheque obterPorId(Cheque filtro) {///carregar
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Cheque> pesquisar(Cheque filtros) {//listar
		return dao.pesquisar(filtros);
	}
	
	public int salvarSequencia(Conta conta, Integer chequeInicial, Integer chequeFinal) throws RNException{
		Cheque cheque = null;
		Cheque chequeVerifica = null;
		ChequeId chequeId = null;
		int contaTotal = 0;

		for (int i = chequeInicial; i <= chequeFinal; i++) {
			chequeId = new ChequeId();
			chequeId.setNnumero(i);
			chequeId.setConta(conta.getCodigo().intValue());
			chequeVerifica = this.obterPorId(new Cheque(chequeId));

			if (chequeVerifica == null) {
				cheque = new Cheque();
				cheque.setId(chequeId);
				cheque.setSituacao(SituacaoCheque.N);
				cheque.setDataCadastro(new Date(System.currentTimeMillis()));
				try {
					salvar(cheque);
				} catch (RNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contaTotal++;
			}
		}
		return contaTotal;
	}//fim metodo
	
	public void cancelarCheque(Cheque cheque) throws RNException 
	{
		if (SituacaoCheque.N.equals(cheque.getSituacao()) || SituacaoCheque.C.equals(cheque.getSituacao())) {
			cheque.setSituacao(SituacaoCheque.C);
			salvar(cheque);
		} else {
			throw new RNException("Não é possível cancelar cheque, status não permitido para operação.");
		}
	}//fim metodo
	
	public void baixarCheque(ChequeId chequeId, Lancamento lancamento) throws RNException{
		Cheque cheque = obterPorId(new Cheque(chequeId));
		if (cheque != null) {
			cheque.setSituacao(SituacaoCheque.B);
			cheque.setLancamento(lancamento);
			try {
				salvar(cheque);
			} catch (RNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}//fim metodo
	
	public void desvinculaLancamento(Conta conta, Integer numeroCheque) throws RNException {
		ChequeId chequeId = new ChequeId();
		chequeId.setNnumero(numeroCheque);
		chequeId.setConta(conta.getCodigo().intValue());
		Cheque cheque = obterPorId(new Cheque(chequeId));
		if (SituacaoCheque.C.equals(cheque.getSituacao())) {
			throw new RNException("Não é possível usar cheque cancelado.");	
		} else {
			cheque.setSituacao(SituacaoCheque.N);
			cheque.setLancamento(null);
			salvar(cheque);
		}
	}//fim metodo
	
}
