package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class Answer {

    public Answer(Double fraction, String text, String t_format, String feedback, String f_format) {
        this.fraction = fraction;
        this.text = text;
        this.t_format = t_format;
        this.feedback = feedback;
        this.f_format = f_format;
    }

    public Double getFraction() {
        return fraction;
    }

    public String getText() {
        return text;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getTextFormat() {
        return t_format;
    }

    public String getFeedbackFormat() {
        return f_format;
    }

    public void setFraction(Double fraction) {
        this.fraction = fraction;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ArrayList<String> getAvailableFractions() {
        return new ArrayList<String>(Arrays.asList(available_fractions));
    }

    private Double fraction;
    private String text, feedback;
    private String t_format, f_format;

    private static String available_fractions[] = { "100.0", "90.0", "83.33333", "80.0", "75.0", "70.0", "66.66667", "60.0", "50.0", "40.0", "33.33333", "30.0", "25.0", "20.0", "16.66667", "14.28571", "12.5", "11.11111", "10.0", "5.0", "0.0", "-5.0", "-10.0", "-11.11111", "-12.5", "-14.28571", "-16.66667", "-20.0", "-25.0", "-30.0", "-33.33333", "-40.0", "-50.0", "-60.0", "-66.66667", "-70.0", "-75.0", "-80.0", "-83.33333", "-90.0", "-100.0" };
}
