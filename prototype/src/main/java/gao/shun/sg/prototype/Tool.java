package gao.shun.sg.prototype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Theodore on 2016/1/4.
 */
public class Tool implements Parcelable {
    public static final Parcelable.Creator<Tool> CREATOR = new Parcelable.Creator<Tool>() {
        public Tool createFromParcel(Parcel source) {
            return new Tool(source);
        }

        public Tool[] newArray(int size) {
            return new Tool[size];
        }
    };
    private static final int DETAILS_COUNT = 3;
    private static final String[] BRANDS = {
            "Ace", "Bosch", "DeWalt", "Irwin", "Jet", "Kreg",
            "Makita", "Porter Cable", "Skil", "Stanley", "Stihl"
    };
    private static final String[] DETAILS_HP = {
            "1/4 HP", "1/2 HP", "3/4 HP", "1 HP", "1 1/2 HP", "2 HP"
    };
    private static final String[] DETAILS_CLAMP_TYPE = {
            "Bar", "Spring", "Quick-Grip", "Pipe", "Parallel"
    };
    private static final String[] DETAILS_INCHES = {
            "2\"", "5\"", "12\"", "18\"", "24\"", "36\"", "48\""
    };
    private static final String[] DETAILS_BATTERY = {
            "12V", "18V", "20V", "24V", "32V", "48V"
    };
    private final String mName;
    private final String mPrice;
    private final String[] mDetails;
    private final String mDescription;

    public Tool(String name, String price, String[] details, String description) {
        mName = name;
        mPrice = price;
        mDetails = new String[DETAILS_COUNT];
        if (details != null) for (int i = 0; i < details.length; i++) mDetails[i] = details[i];
        mDescription = description;
    }

    protected Tool(Parcel in) {
        this.mName = in.readString();
        this.mPrice = in.readString();
        this.mDetails = in.createStringArray();
        this.mDescription = in.readString();
    }

    public String getName() {
        return mName;
    }

    public String getPrice() {
        return mPrice;
    }

    public String[] getDetails() {
        return mDetails;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mPrice);
        dest.writeStringArray(this.mDetails);
        dest.writeString(this.mDescription);
    }

//    public Tool getNewTool(ToolType toolType, ToolPagerAdapter.Tag tab) {
//
//    }
}
