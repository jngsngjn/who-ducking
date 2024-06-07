package hello.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable @Getter
public class Address {

    private String zipcode;
    private String address;

    @Column(name = "detailed_address")
    private String detailedAddress;

    public Address() {
    }

    public Address(String zipcode, String address, String detailedAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailedAddress = detailedAddress;
    }
}