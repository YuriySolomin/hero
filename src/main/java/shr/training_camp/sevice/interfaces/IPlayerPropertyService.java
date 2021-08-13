package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.PlayerProperty;

public interface IPlayerPropertyService<T> extends AbstractEntityService<T> {

    void save (PlayerProperty playerProperty);

    PlayerProperty getPPByPlayerAndProperty(final Long idPlayer, final Long idProperty);
}
