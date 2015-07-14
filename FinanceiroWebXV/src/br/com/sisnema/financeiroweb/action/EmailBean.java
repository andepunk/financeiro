package br.com.sisnema.financeiroweb.action;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.sisnema.financeiroweb.model.Usuario;
import br.com.sisnema.financeiroweb.util.ContextoUtil;
import br.com.sisnema.financeiroweb.util.EmailUtil;
import br.com.sisnema.financeiroweb.util.UtilException;

@ManagedBean
@RequestScoped
public class EmailBean extends ActionBean {

	private String mensagem;

	public void enviarEmail() {
		try {
			
			Usuario usuarioLogado = ContextoUtil.getContextoBean().getUsuarioLogado();
			String assunto = "Suporte ao usuario: "+usuarioLogado.getNome();
			
			EmailUtil eu = new EmailUtil();
			eu.enviarEmail(usuarioLogado.getEmail(), null, assunto, mensagem);
			apresentarMenssagemDeSucesso("Mensagem enviada com sucesso. Em breve entraremos em contato.");
		} catch (UtilException e) {
			apresentarMenssagemDeErro(e.getMessage());
		}
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
