package sotos.apps.message13033;

public class SMS {

    public String transform(String name, String address, String codeNumber) {
        String first = name.trim();
        String second = address.trim();
        String code = codeNumber.trim();
        return code + " " + first + " " + second;
    }

}
