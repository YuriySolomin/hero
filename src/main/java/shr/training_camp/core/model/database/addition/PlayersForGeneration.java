package shr.training_camp.core.model.database.addition;

public interface PlayersForGeneration {

    Long getIdPlayer();
    String getNickName();
    String getFirstName();
    String getLastName();
    String getSecretName();
    int getGender();
    int getPlayerType();
    int getAge();
    String getCode();
    String getDescription();
    int getIsFree();

}
