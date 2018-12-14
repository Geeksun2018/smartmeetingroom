package cn.hephaestus.smartmeetingroom.utils;
import cn.hephaestus.smartmeetingroom.model.User;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatedUtil {
    private static Validator validator= (Validator) Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    public static<T> Boolean validate(T v){
        Set<ConstraintViolation<T>> set= validator.validate(v);
        if (set.size()==0){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args){
        User user=new User();
        user.setUsername("1800738792");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        ValidatedUtil.validate(user);
    }
}
