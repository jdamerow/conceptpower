package edu.asu.conceptpower.validation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.asu.conceptpower.users.IUserManager;
import edu.asu.conceptpower.users.User;
import edu.asu.conceptpower.validation.UserValidator;
import edu.asu.conceptpower.web.backing.UserBacking;
import junit.framework.Assert;

public class UserValidatorTest {
    @Mock
    private IUserManager uManager;
    @InjectMocks
    private UserValidator uValidator;

    private UserBacking validUser;
    private UserBacking invalidUser;
    private User existingUser;
    private UserBacking emptyUser;

    @Before
    public void init() {
        uManager = Mockito.mock(IUserManager.class);
        MockitoAnnotations.initMocks(this);

        validUser = new UserBacking();
        validUser.setUsername("validuser");
        validUser.setEmail("test@abc.xyz");
        validUser.setPassword("password");
        validUser.setRetypedPassword("password");
        validUser.setFullname("Valid User");

        invalidUser = new UserBacking();
        invalidUser.setUsername("Invalid User");
        invalidUser.setEmail("test@abc");
        invalidUser.setPassword("pas");
        invalidUser.setRetypedPassword("password");
        invalidUser.setFullname("Valid9User");

        emptyUser = new UserBacking();
        emptyUser.setUsername("");
        emptyUser.setEmail("");
        emptyUser.setPassword("");
        emptyUser.setRetypedPassword("");
        emptyUser.setFullname("");

        existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("test@abc.xyz");
        existingUser.setPw("password");
        existingUser.setFullname("Valid User");
        Mockito.when(uManager.findUser("username")).thenReturn(existingUser);

    }

    @Test
    public void testValidUserInput() {

        Errors errors = new BindException(validUser, "user");
        ValidationUtils.invokeValidator(uValidator, validUser, errors);
        Assert.assertFalse(errors.hasErrors());
        Assert.assertNull(errors.getFieldError("username"));
        Assert.assertNull(errors.getFieldError("email"));
        Assert.assertNull(errors.getFieldError("password"));
        Assert.assertNull(errors.getFieldError("fullname"));
        Assert.assertNull(errors.getFieldError("retypedPassword"));
    }

    @Test
    public void testInvalidUserInput() {

        Errors errors = new BindException(invalidUser, "user");
        ValidationUtils.invokeValidator(uValidator, invalidUser, errors);
        Assert.assertEquals(5, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "proper.username");
        Assert.assertEquals(errors.getFieldError("email").getCode(), "proper.email");
        Assert.assertEquals(errors.getFieldError("password").getCode(), "short.password");
        Assert.assertEquals(errors.getFieldError("fullname").getCode(), "proper.name");
        Assert.assertEquals(errors.getFieldError("retypedPassword").getCode(), "match.passwords");

    }

    @Test
    public void testEmptyUser() {
        Errors errors = new BindException(emptyUser, "user");
        ValidationUtils.invokeValidator(uValidator, emptyUser, errors);
        Assert.assertEquals(5, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "required.username");
        Assert.assertEquals(errors.getFieldError("email").getCode(), "required.email");
        Assert.assertEquals(errors.getFieldError("password").getCode(), "required.password");
        Assert.assertEquals(errors.getFieldError("retypedPassword").getCode(), "required.password");
        Assert.assertEquals(errors.getFieldError("fullname").getCode(), "required.name");

    }

    @Test
    public void testUserExists() {

        UserBacking eUser = new UserBacking();
        eUser.setUsername(existingUser.getUsername());
        eUser.setPassword(existingUser.getPw());
        eUser.setEmail(existingUser.getEmail());
        eUser.setFullname(existingUser.getFullname());
        eUser.setRetypedPassword("password");
        Errors errors = new BindException(eUser, "user");
        ValidationUtils.invokeValidator(uValidator, eUser, errors);
        Assert.assertEquals(1, errors.getFieldErrorCount());
        Assert.assertEquals(errors.getFieldError("username").getCode(), "exists.username");
        Assert.assertNull(errors.getFieldError("email"));
        Assert.assertNull(errors.getFieldError("password"));
        Assert.assertNull(errors.getFieldError("fullname"));
    }

}
