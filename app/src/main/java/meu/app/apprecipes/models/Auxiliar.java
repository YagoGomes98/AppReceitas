package meu.app.apprecipes.models;
import android.os.Parcel;
import android.os.Parcelable;

public class Auxiliar implements Parcelable {
    private long id;
    private String corpo;
    private long idReceita;

    public Auxiliar(String corpo) {
        this.corpo = corpo;
    }

    public Auxiliar(long id, String corpo, long idReceita) {
        this.id = id;
        this.corpo = corpo;
        this.idReceita = idReceita;
    }

    private Auxiliar(Parcel in) {
        id = in.readLong();
        corpo = in.readString();
        idReceita = in.readLong();
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return corpo;
    }

    public long getIdReceita() {
        return idReceita;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBody(String corpo) {
        this.corpo = corpo;
    }

    public void setIdReceita(long idReceita) {
        this.idReceita = idReceita;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(corpo);
        parcel.writeLong(idReceita);
    }

    public static final Creator<Auxiliar> CREATOR = new Creator<Auxiliar>() {

        @Override
        public Auxiliar createFromParcel(Parcel source) {
            return new Auxiliar(source);
        }

        @Override
        public Auxiliar[] newArray(int size) {
            return new Auxiliar[size];
        }
    };

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", body='" + corpo + '\'' +
                ", recipeId=" + idReceita +
                '}';
    }
}
