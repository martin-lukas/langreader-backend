package net.langreader.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "texts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id")
    private Language language;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Text(String title, String text) {
        this.title = title;
        this.text = text;
    }
}
