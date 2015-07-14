package br.com.sisnema.financeiroweb.negocio;

import java.util.Date;
import java.util.List;

import br.com.sisnema.financeiroweb.dao.ContaDAO;
import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.util.DAOException;
import br.com.sisnema.financeiroweb.util.RNException;

public class ContaRN extends RN<Conta> {

	public ContaRN() {
		super(new ContaDAO());
	}

	@Override
	public void salvar(Conta model) throws RNException {
		try {
			model.setDataCadastro(new Date());
			dao.salvar(model);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public void excluir(Conta model) throws RNException {
		try {
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException(e);
		}
	}

	@Override
	public Conta obterPorId(Conta filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Conta> pesquisar(Conta filtros) {
		return dao.pesquisar(filtros);
	}

	public void tornarFavorita(Conta contaFavorita) throws RNException{
		Conta contaFavoritaExistente = buscarFavorita(contaFavorita.getUsuario());
		
		if(contaFavoritaExistente != null){
			contaFavoritaExistente.setFavorita(false);
			this.salvar(contaFavoritaExistente);
		}
		
		contaFavorita.setFavorita(true);
		salvar(contaFavorita);
	}

	public Conta buscarFavorita(Usuario usuario) {
//		ContaDAO cDAO = (ContaDAO) dao;
		return ((ContaDAO) dao).buscarFavorita(usuario);
	}
}
















