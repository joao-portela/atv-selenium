package br.com.aulaSelenium;

/**
 * Dados usados no cadastro de um usuario. Os valores padrao ja formam um
 * cadastro valido; cada teste altera so o que precisa.
 */
public class Usuario {

	public String titulo = "Mr"; // Mr ou Mrs
	public String nome;
	public String email;
	public String senha = "Senha@123";

	public String dia = "10";
	public String mes = "May";
	public String ano = "1995";

	public boolean newsletter = true;
	public boolean ofertas = true;

	public String primeiroNome = "Joao";
	public String sobrenome = "Silva";
	public String empresa = "Faculdade";
	public String endereco = "Rua das Flores, 123";
	public String endereco2 = "Apto 45";
	public String pais = "India";
	public String estado = "Rio de Janeiro";
	public String cidade = "Niteroi";
	public String cep = "24000-000";
	public String celular = "21999999999";

	public Usuario(String nome, String email) {
		this.nome = nome;
		this.email = email;
	}

	@Override
	public String toString() {
		// aparece no nome do teste parametrizado
		return nome + " <" + email + ">";
	}
}
