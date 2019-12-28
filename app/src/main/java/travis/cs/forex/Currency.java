package travis.cs.forex;

public class Currency {
    private String symbol;
    private String name;
    private double rate;

    Currency(String symbol, String name, double rate){
        this.symbol = symbol;
        this.name = name;
        this.rate = rate;
    }

    public String getSymbol(){
        return symbol;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return symbol + ": " + String.format("%.4f%n", rate);
    }

    public double getRate(){
        return rate;
    }
}
