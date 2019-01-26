package com.example.movieapp.activities.Model;

public class Useful {
    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        return email.matches("[a-zA-Z0-9]+@[a-z]+\\.[a-z]+");
    }


    public static String convertDate(String date) {
        String newDate ="";
        String [] strings = date.split("-");
        for(int i =strings.length -1;i >= 1;i-- )
            newDate = newDate +strings[i] + "-";
        //remember to add the last one
          newDate = newDate +strings[0];
        return newDate;
    }
}
