package gao.shun.sg.prototype;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Theodore on 2016/1/4.
 */
public class ToolGenerator {

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

    private static final String[] PRICE_LOW = {
            "$12.99", "$13.99", "$14.99", "$15.99", "$16.99", "$17.99", "$18.99"
    };

    private static final String[] PRICE_MEDIUM = {
            "$127.99", "$137.99", "$147.99", "$157.99", "$167.99", "$177.99", "$187.99"
    };

    private static final String[] PRICE_HIGH = {
            "$1027.99", "$1037.99", "$1047.99", "$1057.99", "$1067.99", "$1077.99", "$1087.99"
    };

    private final Random mRandom;

    public ToolGenerator() {
        this(0);
    }

    public ToolGenerator(long seed) {
        mRandom = new Random(seed);
    }

    public Tool getNewTool(ToolType toolType, ToolPagerAdapter.Tab tab) {
        final String brand = getRandom(BRANDS);
        String name = brand + " ";
        String price = null;
        final String[] details = new String[3];
        switch (toolType) {
            case CLAMPS:
                details[0] = getRandom(DETAILS_CLAMP_TYPE);
                details[1] = getRandom(DETAILS_INCHES);
                name += details[1] + " " + details[0] + " Clamp";
                details[1] += " opening";
                price = getRandom(PRICE_LOW);
                break;

            case SAWS:
                details[0] = getRandom(DETAILS_INCHES);
                details[1] = getRandom(DETAILS_HP);
                if (tab == ToolPagerAdapter.Tab.BATTERY) details[2] = getRandom(DETAILS_BATTERY);
                if (tab == ToolPagerAdapter.Tab.STATIONARY) name += getRandom(BRANDS);
                else name += getRandom(BRANDS);
                break;

            case DRILLS:
                details[0] = getRandom(DETAILS_HP);
                if (tab == ToolPagerAdapter.Tab.BATTERY) details[1] = getRandom(DETAILS_BATTERY);
                if (tab == ToolPagerAdapter.Tab.STATIONARY) {
                    details[2] = getRandom(DETAILS_INCHES) + " throat";
                    name += getRandom(BRANDS);
                } else {
                    name += "Drill";
                }
                break;

            case SANDERS:
                name += "Sander";
                break;

            case ROUTERS:
                name += "Router";
                break;

            case MORE:
                name += "Tool";
                break;
        }

        if (price == null) {
            if (tab == ToolPagerAdapter.Tab.STATIONARY) price = getRandom(PRICE_HIGH);
            else price = getRandom(PRICE_MEDIUM);
        }

        String description = "The latest and greatest from " + brand + " takes "
                + toolType.name().toLowerCase(Locale.getDefault()) + " to a whole new level. Tenderloin corned beef tail, tongue landjaeger boudin kevin ham pig pork loin short loin shoulder prosciutto ground round. Alcatra salami sausage short ribs t-bone, tongue spare ribs kevin meatball tenderloin. Prosciutto tail meatloaf, chuck pancetta kielbasa leberkas tenderloin drumstick meatball alcatra cow sausage corned beef pork belly. Shoulder swine hamburger tail ham hock bacon pork belly leberkas beef ribs jowl spare ribs.";

        return new Tool(name, price, details, description);
    }

    public ArrayList<Tool> getNewTools(ToolType toolType, ToolPagerAdapter.Tab tab, int count) {
        final ArrayList<Tool> tools = new ArrayList<>(count);
        for (int i = 0; i < count; i++) tools.add(getNewTool(toolType, tab));
        return tools;
    }

    private String getRandom(String[] strings) {
        return strings[mRandom.nextInt(strings.length)];
    }
}
