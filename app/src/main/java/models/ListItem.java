package models;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable {

    int id;
    String grocery_name;
    String grocery_section;
    int grocery_price;
    int quantity = 1;
    Boolean checked = false;

    public ListItem(int id, String grocery_name, String grocery_section, int grocery_price) {
        this.id = id;
        this.grocery_name = grocery_name;
        this.grocery_section = grocery_section;
        this.grocery_price = grocery_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrocery_name() {
        return grocery_name;
    }

    public void setGrocery_name(String grocery_name) {
        this.grocery_name = grocery_name;
    }

    public String getGrocery_section() {
        return grocery_section;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setGrocery_section(String grocery_section) {
        this.grocery_section = grocery_section;
    }

    public int getGrocery_price() {
        return grocery_price;
    }

    public void setGrocery_price(int grocery_price) {
        this.grocery_price = grocery_price;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public ListItem (Parcel in) {
        String[] data = new String[2];
        int[] ints = new int[3];

        in.readStringArray(data);
        in.readIntArray(ints);

        this.id = ints[0];
        this.grocery_name = data[0];
        this.grocery_section = data[1];
        this.grocery_price = ints[1];
        this.quantity = ints[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.grocery_name, this.grocery_section});
        parcel.writeIntArray(new int[] {this.id, this.grocery_price, this.quantity});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    public String toString() {
        return this.grocery_name;
    }
}
