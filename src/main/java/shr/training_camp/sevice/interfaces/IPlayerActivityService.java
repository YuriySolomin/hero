package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.addition.HeroActivityResult;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;

import java.time.LocalDate;
import java.util.List;

public interface IPlayerActivityService<T> extends AbstractEntityService<T> {

    void save(PlayerActivities playerActivities);

    List<PlayerActivityStatistics> findGroupedByAll(final Long idPlayer);

    HeroActivityResult getHeroActivityResult(final Long idPlayer);

    List<PlayerActivityStatistics> getHeroActivityResult(final Long idPlayer, final LocalDate startDate);

    List<PlayerActivityStatistics> getHeroActivityResultByDate(final Long idPlayer, final LocalDate startDate);

    List<PlayerActivityStatistics> getHeroActivityResultByDate2(final Long idActivity, final LocalDate startDate);

    List<PlayerActivities> getAllPlayerActivities();

    List<PlayerActivityStatistics> getGroupedActivitiesForToday(final Long idPlayer);

    List<PlayerActivities> getPlayersActivitiesFilteredByUsed(final Integer isUsed);

    PlayerActivities getPAById(final Long id);

    List<PlayerActivityStatistics> getGroupedByAllParameters(final Long idPlayer, final Long idActivity, final LocalDate startDate, final LocalDate endDate);

    List<PlayerActivityStatistics> getRealPlayerActivityStatistic(final Long idPlayer, final LocalDate startDate, final LocalDate saveDate, final LocalDate endDate);

}
