package br.com.sisnema.financeiroweb.dao;

import org.hibernate.Session;

import br.com.sisnema.financeiroweb.util.HibernateUtil;

public abstract class DAO<T> implements IDAO<T> {

	/**
	 * Como todas as DAOS irão possuir uma sessao, criaremos a mesma
	 * na classe pai, sendo ela HERDADA pelas filhas....
	 */
	protected final Session sessao;
	
	/**
	 * Método construtor de DAO para INICIALIZAR a sessao
	 * do hibernate
	 */
	public DAO() {
		sessao = HibernateUtil.getSessionFactory().getCurrentSession();
	}


}


















