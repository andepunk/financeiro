package br.com.sisnema.financeiroweb.negocio;

import java.util.Date;
import java.util.List;

import br.com.sisnema.financeiroweb.dao.LancamentoDAO;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Lancamento;
import br.com.sisnema.financeiroweb.util.DAOException;
import br.com.sisnema.financeiroweb.util.RNException;

public class LancamentoRN extends RN<Lancamento> {
	
	public LancamentoRN() {
		super(new LancamentoDAO());
	}

	@Override
	public void salvar(Lancamento model) throws RNException {
		try {
			if(model.getCategoria().getPai() == null){
				throw new RNException("Categoria invalida");
			}
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public void excluir(Lancamento lancamento) throws RNException {
		try {
			if(lancamento.getCheque() != null){
				ChequeRN crn = new ChequeRN();
				crn.desvinculaLancamento(lancamento.getConta(), lancamento.getCheque().getId().getNnumero());
			}
			dao.excluir(lancamento);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public Lancamento obterPorId(Lancamento filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Lancamento> pesquisar(Lancamento filtros) {
		return dao.pesquisar(filtros);
	}
	
	public List<Lancamento> pesquisar(Conta c, Date dtInicio, Date dtFim) {
		
//		LancamentoDAO ldao = (LancamentoDAO) dao;
//		ldao.pesquisar(c, dtInicio, dtFim);
		
		return ((LancamentoDAO)dao).pesquisar(c, dtInicio, dtFim);
	}

	public float saldo(Conta conta, Date data) {
		float saldoInicial = conta.getSaldoInicial();
		float saldoNaData = ((LancamentoDAO) dao).saldo(conta, data);
		return saldoInicial + saldoNaData;
	}
}