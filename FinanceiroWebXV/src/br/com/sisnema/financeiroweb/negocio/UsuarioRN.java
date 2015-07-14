package br.com.sisnema.financeiroweb.negocio;

import java.util.List;

import br.com.sisnema.financeiroweb.dao.IDAO;
import br.com.sisnema.financeiroweb.dao.UsuarioDAO;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.util.DAOException;
import br.com.sisnema.financeiroweb.util.RNException;

public class UsuarioRN extends RN<Usuario> {
	
	public UsuarioRN() {
		// Aqui informamos qual instancia sera armazenada no atributo
		// gen�rico dao da classe pai RN.
		// N�O ENTENDEU, da um F3 sob a palavra SUPER
		super(new UsuarioDAO());
	}

	@Override
	public void salvar(Usuario model) throws RNException {
		if(model.getCodigo() == null  || model.getCodigo() ==  0){
			try {
				
				Usuario usuarioExistenteComLogin = buscarPorLogin(model.getLogin());
				
				if(usuarioExistenteComLogin != null){
					throw new RNException("J� existe um usu�rio com o login informado.");
				}
				
				model.getPermissao().add("ROLE_USUARIO");
				dao.salvar(model);
				
				CategoriaRN crn = new CategoriaRN();
				crn.salvaEstruturaPadrao(model);
				
			} catch (DAOException e) {
				throw new RNException("N�o foi poss�vel inserir o usuario. Erro: "+e.getMessage());
			}
		} else {
			try {
				// Transformamos a variavel GENERICA dao em uma variavel
				// mais especifica do tipo UsuarioDAO
				UsuarioDAO userDAO = (UsuarioDAO) dao;
				userDAO.atualizar(model);
				
				// vers�o reduzida dos comandos acima
				// ((UsuarioDAO) dao).atualizar(model);
				
			} catch (DAOException e) {
				throw new RNException("N�o foi poss�vel atualizar o usuario. Erro: "+e.getMessage());
			}
		}
	}

	@Override
	public void excluir(Usuario model) throws RNException {
		try {
			
			new CategoriaRN().excluir(model);
			
			dao.excluir(model);
		} catch (DAOException e) {
			throw new RNException("N�o foi poss�vel excluir o usuario. Erro: "+e.getMessage());
		}
	}

	@Override
	public Usuario obterPorId(Usuario filtro) {
		return dao.obterPorId(filtro);
	}

	@Override
	public List<Usuario> pesquisar(Usuario filtros) {
		return dao.pesquisar(filtros);
	}
	

	public Usuario buscarPorLogin(String login){
		// DownCasting: dao � muito generico e nao sabe dos m�todos
		//			    especificos de UsuarioDAO
		UsuarioDAO userDao = (UsuarioDAO) dao;  
		return userDao.buscarPorLogin(login);
//		return ((UsuarioDAO) dao).buscarPorLogin(login);
	}
	
}



















