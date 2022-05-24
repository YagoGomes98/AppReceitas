package meu.app.apprecipes.models;
import android.os.Parcel;
import android.os.Parcelable;

public class Ingrediente implements Parcelable {
    private long id;
    private String nome;
    private long idReceita;

    public Ingrediente(String nome) {
        this.nome = nome;
    }

    public Ingrediente(long id, String nome, long recipeId) {
        this(nome);
        this.id = id;
        this.idReceita = recipeId;
    }

    private Ingrediente(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        idReceita = in.readLong();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return nome;
    }

    public long getRecipeId() {
        return idReceita;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public void setRecipeId(long idReceita) {
        this.idReceita = idReceita;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nome);
        parcel.writeLong(idReceita);
    }

    public static final Creator<Ingrediente> CREATOR = new Creator<Ingrediente>() {

        @Override
        public Ingrediente createFromParcel(Parcel source) {
            return new Ingrediente(source);
        }

        @Override
        public Ingrediente[] newArray(int size) {
            return new Ingrediente[size];
        }
    };

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + nome + '\'' +
                ", recipeId=" + idReceita +
                '}';
    }
}
