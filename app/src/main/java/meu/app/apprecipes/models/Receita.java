package meu.app.apprecipes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Receita implements Parcelable {
    private long id;
    private String nome;
    private String categoria;
    private String descricao;
    private List<Ingrediente> ingredientes;
    private List<Auxiliar> auxiliar;
    private String imagePath;

    public Receita() {
        ingredientes = new ArrayList<>();
        auxiliar = new ArrayList<>();
    }

    public Receita(String category) {
        this();
        this.categoria = category;
    }

    public Receita(String nome, String categoria, String descricao, String imagePath) {
        this(categoria);
        this.nome = nome;
        this.descricao = descricao;
        this.imagePath = imagePath;
    }

    public Receita(long id, String nome, String categoria, String descricao, String imagePath) {
        this(nome, categoria, descricao, imagePath);
        this.id = id;
    }

    public Receita(String nome, String categoria, String descricao,
                  List<Ingrediente> ingredientes, List<Auxiliar> auxiliar, String imagePath) {
        this(nome, categoria, descricao, imagePath);
        this.ingredientes = ingredientes;
        this.auxiliar = auxiliar;
    }

    public Receita(long id, String nome, String categoria, String descricao,
                  List<Ingrediente> ingredientes, List<Auxiliar> auxiliar, String imagePath) {
        this(nome, categoria, descricao, ingredientes, auxiliar, imagePath);
        this.id = id;
    }

    private Receita(Parcel in) {
        ingredientes = new ArrayList<>();
        auxiliar = new ArrayList<>();

        id = in.readLong();
        nome = in.readString();
        categoria = in.readString();
        descricao = in.readString();
        in.readTypedList(ingredientes, Ingrediente.CREATOR);
        in.readTypedList(auxiliar, Auxiliar.CREATOR);
        imagePath = in.readString();
    }

    public static final Creator<Receita> CREATOR = new Creator<Receita>() {
        @Override
        public Receita createFromParcel(Parcel in) {
            return new Receita(in);
        }

        @Override
        public Receita[] newArray(int size) {
            return new Receita[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public String getCategory() {
        return categoria;
    }

    public void setCategory(String categoria) {
        this.categoria = categoria;
    }

    public String getDescription() {
        return descricao;
    }

    public void setDescription(String descricao) {
        this.descricao = descricao;
    }

    public List<Ingrediente> getIngredients() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<Auxiliar> getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(List<Auxiliar> auxiliar) {
        this.auxiliar = auxiliar;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + nome + '\'' +
                ", category='" + categoria + '\'' +
                ", description='" + descricao + '\'' +
                ", ingredients=" + ingredientes +
                ", directions=" + auxiliar +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nome);
        parcel.writeString(categoria);
        parcel.writeString(descricao);
        parcel.writeTypedList(ingredientes);
        parcel.writeTypedList(auxiliar);
        parcel.writeString(imagePath);
    }
}