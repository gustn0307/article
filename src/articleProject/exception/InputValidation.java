package articleProject.exception;

import java.util.regex.Pattern;

public class InputValidation {
    // 이름 검증: 한글만 허용, 공백 허용 안함
    public void nameCheck (String name) throws MyException {
        boolean check = Pattern.matches("^[가-힣]*$", name); // 한글만 허용, 공백없이

        if (!check) {
            throw new MyException("이름은 공백없이 한글로 입력해주세요");
        }
    }
}