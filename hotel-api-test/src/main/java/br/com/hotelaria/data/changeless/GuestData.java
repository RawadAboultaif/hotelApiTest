package br.com.hotelaria.data.changeless;

public class GuestData {

    public static final String ENDPOINT_LIST_ALL_GUEST = "/guest/list-all-guest";

    public static final String ENDPOINT_BUSCA_POR_CPF_GUEST = "/guest/cpf/";

    public static final String ENDPOINT_ATUALIZAR_GUEST = "/guest/update/";

    public static final String ENDPOINT_GUEST = "/guest/";

    public static final String ID_GUEST = "idGuest";

    public static final String SOCIAL_SECURITY_NUMBER_GUEST = "socialSecurityNumber";

    public static final String CURRENT_DATE_AFTER_END_DATE = "Birthday must be in the past";

    public static final String AGE_UNDER_EIGHTEEN = "Age under eighteen";

    public static final String NAME_NOT_EMPTY = "Name cannot be empty";
    public static final String PHONE_NOT_EMPTY = "Phone cannot be empty";
    public static final String CPF_INVALID = "Cpf is invalid";
    public static final String BIRTHDAY_NOT_EMPTY = "Birthday cannot be null";
    public static final String EMAIL_NOT_EMPTY = "Email cannot be empty";
}
