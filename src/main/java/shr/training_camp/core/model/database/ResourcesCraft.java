package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_resources_craft")
@NoArgsConstructor
@Data
public class ResourcesCraft {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_craft")
    private Long idCraft;
    @Column(name = "id_new_resource")
    private Long idNewResource;
    @Column(name = "new_quantity")
    private Long newQuantity;
    @Column(name = "id_first")
    private Long idFirst;
    @Column(name = "first_quantity")
    private Long firstQuantity;
    @Column(name = "id_second")
    private Long idSecond;
    @Column(name = "second_quantity")
    private Long secondQuantity;
    @Column(name = "id_third")
    private Long idThird;
    @Column(name = "third_quantity")
    private Long thirdQuantity;
    @Column(name = "id_forth")
    private Long idForth;
    @Column(name = "forth_quantity")
    private Long forthQuantity;
    @Column(name = "id_fifth")
    private Long idFifth;
    @Column(name = "fifth_quantity")
    private Long fifthQuantity;
    @Column(name = "id_rule")
    private Long idRule;
    @Column(name = "is_enabled")
    private Long isEnabled;

    public ResourcesCraft(final Long idNewResource, final Long newQuantity, final Long idFirst, final Long firstQuantity) {
        this.idNewResource = idNewResource;
        this.newQuantity = newQuantity;
        this.idFirst = idFirst;
        this.firstQuantity = firstQuantity;
    }

    public ResourcesCraft(final Long idNewResource, final Long newQuantity, final Long idFirst, final Long firstQuantity
            , final Long idSecond, final Long secondQuantity) {
        this(idNewResource, newQuantity, idFirst, firstQuantity);
        this.idSecond = idSecond;
        this.secondQuantity = secondQuantity;
    }

    public ResourcesCraft(final Long idNewResource, final Long newQuantity, final Long idFirst, final Long firstQuantity
            , final Long idSecond, final Long secondQuantity, final Long idThird, final Long thirdQuantity) {
        this(idNewResource, newQuantity, idFirst, firstQuantity, idSecond, secondQuantity);
        this.idThird = idThird;
        this.thirdQuantity = thirdQuantity;
    }

    public ResourcesCraft(final Long idNewResource, final Long newQuantity, final Long idFirst, final Long firstQuantity
            , final Long idSecond, final Long secondQuantity, final Long idThird, final Long thirdQuantity
            , final Long idForth, final Long forthQuantity) {
        this(idNewResource, newQuantity, idFirst, firstQuantity, idSecond, secondQuantity, idThird, thirdQuantity);
        this.idForth = idForth;
        this.forthQuantity = forthQuantity;
    }

    public ResourcesCraft(final Long idNewResource, final Long newQuantity, final Long idFirst, final Long firstQuantity
            , final Long idSecond, final Long secondQuantity, final Long idThird, final Long thirdQuantity
            , final Long idForth, final Long forthQuantity, final Long idFifth, final Long fifthQuantity) {
        this(idNewResource, newQuantity, idFirst, firstQuantity, idSecond, secondQuantity, idThird, thirdQuantity,
                idForth, forthQuantity);
        this.idFifth = idFifth;
        this.fifthQuantity = fifthQuantity;
    }

}
