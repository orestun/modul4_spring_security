package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class GiftCertificate {
    @Id
    @JsonView
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @NotNull(message = "Name field for gift certificate can`t be null")
    @NotBlank(message = "Name field for gift certificate should be input using letters A-Z or/and numbers 0-9")
    @Size(max = 30, message = "Name field length for gift certificate can`t be more than 30 chars")
    @JsonView
    @Column(length = 30)
    private String name;

    @NotNull(message = "Description field for gift certificate can`t be null")
    @NotBlank(message = "Description field for gift certificate should be input using letters A-Z or/and numbers 0-9")
    @Size(max = 200, message = "Description field length for gift certificate can`t be more than 30 chars")
    @JsonView
    @Column(length = 200)
    private String description;

    @NotNull(message = "Duration field for gift certificate can`t be null")
    @Min(value = 1, message = "Duration field for gift certificate can`t be less than 1")
    @JsonView
    private int duration;

    @NotNull(message = "Price field for gift certificate can`t be null")
    @Min(value = 0, message = "Price field for gift certificate can`t be negative")
    @JsonView
    @Digits(integer=8, fraction=2)
    private BigDecimal price;

    @CreatedDate
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate createDate;

    @LastModifiedDate
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate updateDate;


    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    public GiftCertificate(String name, String description, int duration, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
    }

    public GiftCertificate(long id, String name, String description, int duration, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate that)) return false;
        return getDuration() == that.getDuration() && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getCreateDate(), that.getCreateDate()) && Objects.equals(getUpdateDate(), that.getUpdateDate()) && Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getDuration(), getPrice(), getCreateDate(), getUpdateDate(), getTags());
    }
}
