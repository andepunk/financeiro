package br.com.sisnema.financeiroweb.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.util.DAOException;

public class ContaDAO extends DAO<Conta> {


	@Override
	public void salvar(Conta model) throws DAOException {
		sessao.saveOrUpdate(model);
	}

	@Override
	public void excluir(Conta model) throws DAOException {
		sessao.delete(model);
	}

	@Override
	public Conta obterPorId(Conta filtro) {
		return (Conta) sessao.get(Conta.class, filtro.getCodigo());
	}

	@Override
	public List<Conta> pesquisar(Conta filtros) {
		Criteria criteria = sessao.createCriteria(Conta.class);
		criteria.add(Restrictions.eq("usuario", filtros.getUsuario()));
		return criteria.list();
	}
	
	public Conta buscarFavorita(Usuario us) {
		Criteria criteria = sessao.createCriteria(Conta.class);
		
		criteria.add(Restrictions.eq("usuario", us));
		criteria.add(Restrictions.eq("favorita", true));
		
		return (Conta) criteria.uniqueResult();
	}

}








