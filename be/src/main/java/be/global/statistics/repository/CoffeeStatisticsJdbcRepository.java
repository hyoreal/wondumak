package be.global.statistics.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import be.global.statistics.entity.CoffeeStatistics;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeStatisticsJdbcRepository {
	private final JdbcTemplate jdbcTemplate;
	private Integer batchSize = 10000;

	public void saveAll(List<CoffeeStatistics> coffeeStatisticsList) {

		Integer batchCount = 0;

		List<CoffeeStatistics> subList = new ArrayList<>();

		for (int i = 0; i < coffeeStatisticsList.size(); i++) {

			subList.add(coffeeStatisticsList.get(i));

			if ((i + 1) % batchSize == 0) {
				batchCount = batchInsert(batchCount, subList);
			}
		}
		if (!subList.isEmpty()) {
			batchCount = batchInsert(batchCount, subList);
		}
		System.out.println("batchCount: " + batchCount);
	}

	private Integer batchInsert(Integer batchCount, List<CoffeeStatistics> subList) {

		jdbcTemplate.batchUpdate("INSERT INTO BEER_STATISTICS (created_at, date, week, coffee_id, kor_name,"
				+ "category1, category2, total_average_stars, female_average_stars, male_average_stars, tag1,"
				+ "tag2, tag3, tag4, view_count, rating_count)" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
					CoffeeStatistics coffeeStatistics = subList.get(i);
					preparedStatement.setTimestamp(1, Timestamp.valueOf(coffeeStatistics.getCreatedAt()));
					preparedStatement.setDate(2, Date.valueOf(coffeeStatistics.getDate()));
					preparedStatement.setInt(3, coffeeStatistics.getWeek());
					preparedStatement.setLong(4, coffeeStatistics.getCoffeeId());
					preparedStatement.setString(5, coffeeStatistics.getKorName());
					preparedStatement.setString(6, coffeeStatistics.getCategory1());
					preparedStatement.setString(7, coffeeStatistics.getCategory2());
					preparedStatement.setDouble(8, coffeeStatistics.getCoffeeDetailsStars().getTotalAverageStars());
					preparedStatement.setDouble(9, coffeeStatistics.getCoffeeDetailsStars().getFemaleAverageStars());
					preparedStatement.setDouble(10, coffeeStatistics.getCoffeeDetailsStars().getMaleAverageStars());
					// if (coffeeStatistics.getCoffeeDetailsTopTags() != null) {
					preparedStatement.setString(11, coffeeStatistics.getCoffeeDetailsTopTags().getTag1());
					preparedStatement.setString(12, coffeeStatistics.getCoffeeDetailsTopTags().getTag2());
					preparedStatement.setString(13, coffeeStatistics.getCoffeeDetailsTopTags().getTag3());
					preparedStatement.setString(14, coffeeStatistics.getCoffeeDetailsTopTags().getTag4());
					// }
					preparedStatement.setLong(15, coffeeStatistics.getViewCount());
					preparedStatement.setLong(16, coffeeStatistics.getRatingCount());
				}

				@Override
				public int getBatchSize() {
					return subList.size();
				}
			});
		subList.clear();
		batchCount++;
		return batchCount;
	}
}
