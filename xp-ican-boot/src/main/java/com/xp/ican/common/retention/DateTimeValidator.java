package com.xp.ican.common.retention;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateTimeValidator implements ConstraintValidator<MyDateTime,String> {

    private MyDateTime myDateTime;

    @Override
    public void initialize(MyDateTime myDateTime){
        this.myDateTime=myDateTime;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(s==null){
            return true;
        }
        String format=myDateTime.format();
        if(s.length()!=format.length()){
            return false;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        try{
            simpleDateFormat.parse(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
