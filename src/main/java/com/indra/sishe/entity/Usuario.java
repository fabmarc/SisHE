package com.indra.sishe.entity;

import java.io.Serializable;

public class Usuario implements Serializable, Comparable<Usuario> {

	private static final long serialVersionUID = -4353769998918846888L;

	private Long id;

	private Cargo cargo;

	private Sindicato sindicato;

	private Cidade cidade;

	private String nome;

	private String login;

	private String senha;

	private String senhaConfirm;

	private Integer matricula;

	private String email;

	public Usuario() {

	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public Cargo getCargo() {

		return cargo;
	}

	public void setCargo(Cargo cargo) {

		this.cargo = cargo;
	}

	public Sindicato getSindicato() {

		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {

		this.sindicato = sindicato;
	}

	public Cidade getCidade() {

		return cidade;
	}

	public void setCidade(Cidade cidade) {

		this.cidade = cidade;
	}

	public String getNome() {

		return nome;
	}

	public void setNome(String nome) {

		this.nome = nome;
	}

	public String getLogin() {

		return login;
	}

	public void setLogin(String login) {

		this.login = login;
	}

	public String getSenha() {

		return senha;
	}

	public void setSenha(String senha) {

		this.senha = senha;
	}

	public String getSenhaConfirm() {

		return senhaConfirm;
	}

	public void setSenhaConfirm(String senhaConfirm) {

		this.senhaConfirm = senhaConfirm;
	}

	public Integer getMatricula() {

		return matricula;
	}

	public void setMatricula(Integer matricula) {

		this.matricula = matricula;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (nome == null) {
			if (other.nome != null) return false;
		} else if (!nome.equals(other.nome)) return false;
		return true;
	}

	@Override
	public int compareTo(Usuario o) {

		int valor = nome.toLowerCase().compareTo(o.nome.toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}
	}

}
