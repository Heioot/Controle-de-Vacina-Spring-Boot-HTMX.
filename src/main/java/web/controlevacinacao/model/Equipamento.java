package web.controlevacinacao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Equipamento {

    @Id
    @SequenceGenerator(name="equipamento_seq_gen", sequenceName="equipamento_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="equipamento_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "O nome do equipamento é obrigatório")
    private String nome;
    @NotBlank(message = "A descrição do equipamento é obrigatória")
    private String descricao;

    @Enumerated(EnumType.STRING) // Define que o enum será armazenado como texto
    private Status status = Status.ATIVO;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipamento that = (Equipamento) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
