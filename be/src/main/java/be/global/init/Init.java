package be.global.init;

import static org.apache.commons.io.FileUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.controller.CoffeeController;
import be.domain.coffee.dto.CoffeeDto;
import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeDetailsBestRating;
import be.domain.coffee.entity.CoffeeDetailsStars;
import be.domain.coffee.entity.MonthlyCoffee;
import be.domain.coffee.entity.WeeklyCoffee;
import be.domain.coffee.entity.WeeklyCoffeeCategory;
import be.domain.coffee.repository.CoffeeRepository;
import be.domain.coffee.repository.MonthlyCoffeeRepository;
import be.domain.coffee.repository.WeeklyCoffeeRepository;
import be.domain.coffee.service.CoffeeService;
import be.domain.coffeecategory.dto.CoffeeCategoryDto;
import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.entity.CoffeeCategoryType;
import be.domain.coffeecategory.repository.CoffeeCategoryRepository;
import be.domain.coffeecategory.service.CoffeeCategoryService;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;
import be.domain.coffeetag.repository.CoffeeTagRepository;
import be.domain.coffeetag.service.CoffeeTagService;
import be.domain.user.entity.User;
import be.domain.user.entity.UserCoffeeCategory;
import be.domain.user.entity.UserCoffeeTag;
import be.domain.user.entity.enums.Age;
import be.domain.user.entity.enums.Gender;
import be.domain.user.entity.enums.RandomProfile;
import be.domain.user.entity.enums.Role;
import be.domain.user.entity.enums.UserStatus;
import be.domain.user.repository.UserRepository;
import be.global.statistics.entity.TotalStatistics;
import be.global.statistics.repository.TotalStatisticsRepository;

// @Configuration
public class Init {

	private static final Logger log = LoggerFactory.getLogger(Init.class);

	private final PasswordEncoder passwordEncoder;

	public Init(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	private final List<String> pairingCategoryList =
		List.of("FRIED", "GRILL", "STIR", "FRESH", "DRY", "SNACK", "SOUP", "ETC");

	@Bean
	@Transactional
	CommandLineRunner stubInit(CoffeeController coffeeController, CoffeeService coffeeService,
		CoffeeRepository coffeeRepository, CoffeeTagService coffeeTagService,
		CoffeeCategoryRepository coffeeCategoryRepository, UserRepository userRepository,
		CoffeeTagRepository coffeeTagRepository, TotalStatisticsRepository totalStatisticsRepository,
		CoffeeCategoryService coffeeCategoryService, MonthlyCoffeeRepository monthlyCoffeeRepository,
		WeeklyCoffeeRepository weeklyCoffeeRepository) throws IOException {

		for (int i = 0; i < 8; i++) {
			CoffeeCategory coffeeCategory = CoffeeCategory.builder()
				.coffeeCategoryType(CoffeeCategoryType.values()[i])
				.build();
			coffeeCategoryRepository.save(coffeeCategory);
		}

		for (int i = 0; i < 16; i++) {
			CoffeeTag coffeeTag = CoffeeTag.builder()
				.coffeeTagType(CoffeeTagType.values()[i])
				.build();
			coffeeTagRepository.save(coffeeTag);
		}

		for (int i = 0; i < 1; i++) {
			TotalStatistics totalStatistics =
				TotalStatistics.builder()
					.totalCoffeeViewCount(0)
					.totalPairingCount(0)
					.totalRatingCount(0)
					.totalVisitorCount(0)
					.date(LocalDate.now())
					.build();

			totalStatisticsRepository.save(totalStatistics);
		}

		/*
		 * BEER STUB DATA
		 */

		ClassLoader classLoader = getClass().getClassLoader();

		// String FILE_PATH = "src/main/java/be/global/init/Wondumak_Products.csv";

		List<List<String>> csvList = new ArrayList<List<String>>();

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Wondumak_Products.csv");

		File csv = convertInputStreamToFile(inputStream);
		// File csv = new File(FILE_PATH);
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(csv));
			while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
				List<String> aLine = new ArrayList<String>();
				String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
				aLine = Arrays.asList(lineArr);
				csvList.add(aLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close(); // 사용 후 BufferedReader를 닫아준다.
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 1; i < csvList.size(); i++) {

			CoffeeDetailsStars coffeeDetailsStars =
				CoffeeDetailsStars.builder()
					.totalAverageStars(0.0)
					.femaleAverageStars(0.0)
					.maleAverageStars(0.0)
					.build();

			List<String> list = csvList.get(i);

			CoffeeDto.Post.PostBuilder postBuilder = CoffeeDto.Post.builder();

			postBuilder.korName(list.get(0));
			postBuilder.engName(list.get(1));
			postBuilder.country(list.get(2));
			if (!list.get(4).contains("")) {
				postBuilder.coffeeCategories(List.of(
					CoffeeCategoryDto.Response.builder()
						.coffeeCategoryType(CoffeeCategoryType.valueOf(list.get(3)))
						.build(),
					CoffeeCategoryDto.Response.builder()
						.coffeeCategoryType(CoffeeCategoryType.valueOf(list.get(4)))
						.build()
				));
			} else {
				postBuilder.coffeeCategories(List.of(
					CoffeeCategoryDto.Response.builder()
						.coffeeCategoryType(CoffeeCategoryType.valueOf(list.get(3)))
						.build()));
			}
			if (list.get(5).isEmpty()) {
				postBuilder.roasting(0.0);
			} else {
				postBuilder.roasting(Double.valueOf(list.get(5)));
			}
			if (!list.get(6).isBlank()) {
				postBuilder.acidity(Integer.valueOf(list.get(6)));
			}
			postBuilder.thumbnail(list.get(7));

			coffeeController.postCoffee(postBuilder.build());

			Coffee findCoffee = coffeeService.findVerifiedCoffee((long)i);

			findCoffee.addCoffeeDetailsStars(coffeeDetailsStars);

			findCoffee.getCoffeeDetailsStatistics()
				.updateBestPairingCategory(pairingCategoryList.get((int)((Math.random() * 7))));

			coffeeRepository.save(findCoffee);
		}

		/*
		 * MONTHLY BEER STUB DATA
		 */
		for (int i = 0; i < 5; i++) {
			Long rand = (long)(Math.random() * 219 + 1);

			Coffee findCoffee = coffeeService.findVerifiedCoffee(rand);

			MonthlyCoffee monthlyCoffee = MonthlyCoffee.builder().build();

			monthlyCoffee.create(findCoffee);

			CoffeeDetailsBestRating bestRating =
				CoffeeDetailsBestRating.builder()
					.bestRatingId(rand)
					.bestUserId((long)i)
					.bestNickname("닉네임 " + i)
					.profileImage(RandomProfile.values()[(int)(Math.random() * 4)].getValue())
					.bestStar(4.5)
					.bestContent("레이팅 " + i)
					.bestLikeCount(25)
					.build();

			monthlyCoffee.addBestRating(bestRating);

			monthlyCoffeeRepository.save(monthlyCoffee);
		}

		/*
		 * WEEKLY BEER STUB DATA
		 */
		for (int i = 0; i < 5; i++) {
			Long rand = (long)(Math.random() * (csvList.size() - 1) + 1);

			Coffee findCoffee = coffeeService.findVerifiedCoffee(rand);

			WeeklyCoffeeCategory weeklyCoffeeCategory = new WeeklyCoffeeCategory("ARABICA", "ROBUSTA");

			WeeklyCoffee.WeeklyCoffeeBuilder weeklyCoffee = WeeklyCoffee.builder();

			weeklyCoffee.coffeeId(findCoffee.getId());
			weeklyCoffee.korName(findCoffee.getCoffeeDetailsBasic().getKorName());
			weeklyCoffee.country(findCoffee.getCoffeeDetailsBasic().getCountry());
			weeklyCoffee.thumbnail(findCoffee.getCoffeeDetailsBasic().getThumbnail());
			weeklyCoffee.weeklyCoffeeCategory(weeklyCoffeeCategory);
			weeklyCoffee.roasting(findCoffee.getCoffeeDetailsBasic().getRoasting());
			weeklyCoffee.acidity(findCoffee.getCoffeeDetailsBasic().getAcidity());
			weeklyCoffee.averageStar(findCoffee.getCoffeeDetailsStars().getTotalAverageStars());

			weeklyCoffeeRepository.save(weeklyCoffee.build());
		}

		/*
		 * BEER STUB DATA
		 */
		// for (int i = 1; i <= 30; i++) {
		//
		// 	int rand7 = (int)(Math.random() * 7);
		//
		// 	CoffeeCategoryDto.Response coffeeCategoryDto =
		// 		CoffeeCategoryDto.Response.builder()
		// 			.coffeeCategoryId((long)rand7 + 1)
		// 			.coffeeCategoryType(CoffeeCategoryType.values()[rand7])
		// 			.build();
		//
		// 	CoffeeDto.Post postCoffee =
		// 		CoffeeDto.Post.builder()
		// 			.korName("한글 이름" + i)
		// 			.engName("EngName" + i)
		// 			.country("Germany")
		// 			.coffeeCategories(List.of(coffeeCategoryDto))
		// 			.thumbnail("썸네일 이미지 경로" + i)
		// 			.abv(4.5)
		// 			.ibu(20)
		// 			.build();
		//
		// 	CoffeeDetailsStars coffeeDetailsStars =
		// 		CoffeeDetailsStars.builder()
		// 			.totalAverageStars((double)(int)((Math.random() * 5) * 100) / 100)
		// 			.femaleAverageStars((double)(int)((Math.random() * 5) * 100) / 100)
		// 			.maleAverageStars((double)(int)((Math.random() * 5) * 100) / 100)
		// 			.build();
		//
		// 	coffeeController.postCoffee(postCoffee);
		//
		// 	Coffee findCoffee = coffeeService.findVerifiedCoffee((long)i);
		// 	findCoffee.addCoffeeDetailsCounts(BEER_DETAILS_COUNTS);
		// 	findCoffee.addCoffeeDetailsStars(coffeeDetailsStars);
		//
		// 	coffeeRepository.save(findCoffee);
		// }

		/*
		 * RATING STUB DATA
		 */
		// for (int i = 1; i <= 100; i++) {
		//
		// 	RatingRequestDto.Post ratingPost = RatingRequestDto.Post
		// 		.builder()
		// 		.coffeeId((long)(Math.random() * 30) + 1)
		// 		.
		//
		// 	private Long coffeeId;
		// 	private String nickname;
		// 	private String content;
		// 	private Double star;
		// 	private String color;
		// 	private String taste;
		// 	private String flavor;
		// 	private String carbonation;
		// }

		/*
		 * USER STUB DATA
		 */

		for (int i = 1; i <= 2; i++) {
			User user = User.builder()
				.email("e" + i + "@mail.com")
				.provider("LOCAL")
				.nickname("닉네임" + i)
				.roles(List.of(Role.ROLE_ADMIN.toString()))
				.password(passwordEncoder.encode("password" + i + "!"))
				.status(UserStatus.ACTIVE_USER.getStatus())
				.imageUrl(RandomProfile.values()[(int)(Math.random() * 4)].getValue())
				.build();

			// user.putUserInfo(Age.values()[(int)(Math.random() * 6)], Gender.values()[(int)(Math.random() * 3)]);
			user.putUserInfo(Age.values()[(int)(Math.random() * 6)], Gender.MALE);
			user.putUserCoffeeTags(new ArrayList<>());

			for (int j = 0; j < 4; j++) {
				UserCoffeeTag userCoffeeTag =
					UserCoffeeTag.builder()
						.coffeeTag(coffeeTagService.findVerifiedCoffeeTag((long)(Math.random() * 16) + 1))
						.user(user)
						.build();
				user.addUserCoffeeTags(userCoffeeTag);
			}

			user.putUserCoffeeCategories(new ArrayList<>());

			for (int j = 0; j < 2; j++) {
				UserCoffeeCategory userCoffeeCategory =
					UserCoffeeCategory.builder()
						.coffeeCategory(coffeeCategoryService.findVerifiedCoffeeCategoryById((long)(Math.random() * 7) + 1))
						.user(user)
						.build();

				user.addUserCoffeeCategories(userCoffeeCategory);
			}
			userRepository.save(user);
		}

		for (int i = 3; i <= 20; i++) {

			User user = User.builder()
				.email("e" + i + "@mail.com")
				.provider("LOCAL")
				.nickname("닉네임" + i)
				.roles(List.of(Role.ROLE_USER.toString()))
				.password(passwordEncoder.encode("password" + i + "!"))
				.status(UserStatus.ACTIVE_USER.getStatus())
				.imageUrl(RandomProfile.values()[(int)(Math.random() * 4)].getValue())
				.build();

			// user.putUserInfo(Age.values()[(int)(Math.random() * 6)], Gender.values()[(int)(Math.random() * 3)]);
			user.putUserInfo(Age.values()[(int)(Math.random() * 6)], Gender.MALE);
			user.putUserCoffeeTags(new ArrayList<>());

			for (int j = 0; j < 4; j++) {
				UserCoffeeTag userCoffeeTag =
					UserCoffeeTag.builder()
						.coffeeTag(coffeeTagService.findVerifiedCoffeeTag((long)(Math.random() * 16) + 1))
						.user(user)
						.build();
				user.addUserCoffeeTags(userCoffeeTag);
			}

			user.putUserCoffeeCategories(new ArrayList<>());

			for (int j = 0; j < 2; j++) {
				UserCoffeeCategory userCoffeeCategory =
					UserCoffeeCategory.builder()
						.coffeeCategory(coffeeCategoryService.findVerifiedCoffeeCategoryById((long)(Math.random() * 7) + 1))
						.user(user)
						.build();

				user.addUserCoffeeCategories(userCoffeeCategory);
			}
			userRepository.save(user);
		}

		return null;
	}

	public static File convertInputStreamToFile(InputStream inputStream) throws IOException {

		File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".csv");
		tempFile.deleteOnExit();

		copyInputStreamToFile(inputStream, tempFile);

		return tempFile;
	}

}
