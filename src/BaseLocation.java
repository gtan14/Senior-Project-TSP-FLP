//  contains the top 3 base locations for trucks to start
public class BaseLocation{
    private String first, second, third;

    public BaseLocation(){

    }

    public BaseLocation(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /*
        Getter and Setters
         */
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }
}