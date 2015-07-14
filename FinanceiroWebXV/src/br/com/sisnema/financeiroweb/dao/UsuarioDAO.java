package br.com.sisnema.financeiroweb.dao;

import java.util.List;

import org.hibernate.Query;

import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.util.DAOException;

public class UsuarioDAO extends DAO<Usuario> {

	@Override
	public void salvar(Usuario model) throws DAOException {
		sessao.save(model);
	}
	
	public void atualizar(Usuario model) throws DAOException {
		
		if(model.getPermissao() == null || model.getPermissao().size() == 0){
			Usuario usuarioPermissao = obterPorId(model);
			model.setPermissao(usuarioPermissao.getPermissao());
			sessao.evict(usuarioPermissao);
		}
		
		sessao.update(model);
	}

	@Override
	public void excluir(Usuario model) throws DAOException {
		sessao.delete(model);
	}

	@Override
	public Usuario obterPorId(Usuario filtro) {
		return (Usuario) sessao.get(Usuario.class, filtro.getCodigo());
	}

	@Override
	public List<Usuario> pesquisar(Usuario filtros) {
		return sessao.createCriteria(Usuario.class).list();
	}
	
	public Usuario buscarPorLogin(String login){
		String hql = "select u from Usuario u where u.login = :login";
		Query consulta = sessao.createQuery(hql);
		consulta.setString("login", login);
		
		return (Usuario) consulta.uniqueResult();
	}
}









