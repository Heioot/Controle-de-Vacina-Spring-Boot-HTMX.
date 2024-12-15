package web.controlevacinacao.filter;

public class EquipamentoFilter {

    private Long codigo;
    private String nome;
    private String descricao;
    private String status;

    // Getters e Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo + "\nnome: " + nome + "\ndescricao: " + descricao + "\nstatus: " + status;
    }
}
