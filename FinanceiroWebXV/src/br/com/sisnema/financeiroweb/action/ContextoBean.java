package br.com.sisnema.financeiroweb.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import br.com.sisnema.financeiroweb.model.Conta;
import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.negocio.ContaRN;
import br.com.sisnema.financeiroweb.negocio.UsuarioRN;
import br.com.sisnema.financeiroweb.util.RNException;

/**
 * Objetivo desta classe é conter os dados do usuario logado
 * armazendo em um único objeto tudo que possa ser necessario,
 * o qual estará em escopo de sessão.
 */
@ManagedBean
@SessionScoped
public class ContextoBean {

	private Usuario	usuarioLogado = null;
	private Conta	contaAtiva	  = null;

	private Locale localizacao 	  = null;
	private List<Locale> idiomas;

	public Usuario getUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String login = external.getRemoteUser();

		if (this.usuarioLogado == null || !login.equals(this.usuarioLogado.getLogin())) {

			if (login != null) {
				UsuarioRN usuarioRN = new UsuarioRN();
				this.usuarioLogado = usuarioRN.buscarPorLogin(login);
				this.contaAtiva = null;

				String[] info = this.usuarioLogado.getIdioma().split("_");
				Locale locale = new Locale(info[0], info[1]);
				context.getViewRoot().setLocale(locale);
			}
		}
		return usuarioLogado;
	}
	
	public void setUsuarioLogado(Usuario usuario) {
		this.usuarioLogado = usuario;
	}	
	

	public Conta getContaAtiva() {
		if (this.contaAtiva == null) {
			Usuario usuario = this.getUsuarioLogado();

			ContaRN contaRN = new ContaRN();
			this.contaAtiva = contaRN.buscarFavorita(usuario);

			if (this.contaAtiva == null) {
				List<Conta> contas = contaRN.pesquisar(new Conta(usuario));
				if (contas != null) {
					for (Conta conta : contas) {
						this.contaAtiva = conta;
						break;
					}
				}
			}
		}
		return this.contaAtiva;
	}

	public void setContaAtiva(ValueChangeEvent event) {
		Integer codigo = (Integer) event.getNewValue();

		ContaRN contaRN = new ContaRN();
		this.contaAtiva = contaRN.obterPorId(new Conta(codigo));
	}
	public Locale getLocaleUsuario() {
		if (localizacao == null) {
			Usuario usuario = getUsuarioLogado();
			String idioma = usuario.getIdioma();
			String[] info = idioma.split("_");
			localizacao = new Locale(info[0], info[1]);
		}		
		
		return localizacao;
	}
	
	
	/**
	 * Retorna uma lista de Locale (idiomas suportados pelo sistema, 
	 * definido no arquivo faces-config.xml)
	 */
	public List<Locale> getIdiomas() {
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<Locale> locales = context.getApplication().getSupportedLocales();
		idiomas = new ArrayList<Locale>();
		
		while(locales.hasNext()) {
			idiomas.add(locales.next());
		}
		
		return idiomas;
	}
	
	/**
	 * Altera o idioma default do usuario na base de dados
	 * para que sempre fique nesse idioma (até ser alterado)
	 */
	public void setIdiomaUsuario(String idioma) {
		UsuarioRN usuarioRN = new UsuarioRN();
		try {
			usuarioLogado = usuarioRN.obterPorId(getUsuarioLogado());
			usuarioLogado.setIdioma(idioma);
			usuarioRN.salvar(usuarioLogado);
		} catch (RNException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao setar o idioma: " + e.getMessage(), null));
			return;
		}

		// APÓS ter alterado o idioma do usuario
		// seta o novo idioma no locale da sessao
		String[] info = idioma.split("_");
		Locale locale = new Locale(info[0], info[1]);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(locale);		
	}
}














