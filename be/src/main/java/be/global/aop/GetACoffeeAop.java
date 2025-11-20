package be.global.aop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeCoffeeCategory;
import be.domain.coffee.entity.CoffeeCoffeeTag;
import be.domain.coffee.repository.CoffeeQueryRepository;
import be.domain.coffee.repository.CoffeeRepository;
import be.domain.coffee.service.CoffeeService;
import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.repository.CoffeeCategoryRepository;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;
import be.domain.coffeetag.repository.CoffeeTagRepository;
import be.domain.coffeetag.service.CoffeeTagService;
import be.domain.pairing.entity.Pairing;
import be.domain.rating.entity.Rating;
import be.domain.rating.entity.RatingTag;
import be.domain.rating.service.RatingService;
import be.domain.user.entity.User;
import be.domain.user.entity.enums.Gender;
import be.domain.user.service.UserService;
import be.global.statistics.entity.TotalStatistics;
import be.global.statistics.repository.TotalStatisticsQueryRepository;
import be.global.statistics.repository.TotalStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WondumakAop {

	private final UserService userService;
	private final CoffeeService coffeeService;
	private final CoffeeTagService coffeeTagService;
	private final RatingService ratingService;
	private final CoffeeRepository coffeeRepository;
	private final CoffeeCategoryRepository coffeeCategoryRepository;
	private final CoffeeTagRepository coffeeTagRepository;
	private final CoffeeQueryRepository coffeeQueryRepository;
	private final TotalStatisticsRepository totalStatisticsRepository;
	private final TotalStatisticsQueryRepository totalStatisticsQueryRepository;

	@Before(value = "Pointcuts.getWeeklyCoffee()")
	public void calculateVisitorByMainPageView(JoinPoint joinPoint) {

		TotalStatistics totalStatistics = totalStatisticsQueryRepository.findTotalStatistics();

		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();

		HttpServletRequest request = attr.getRequest(); // Http Request
		HttpServletResponse response = attr.getResponse(); // Http Response
		Cookie[] cookies = request.getCookies(); // Request Cookies
		String token = request.getHeader("Cookie"); // Cookie에서 뜯어온 토큰들

		if (cookies != null) { // 쿠키를 가진 경우

			for (Cookie cookie : cookies) {

				/*
				 * 통계 쿠키가 있는데 오늘 방문한 적이 없을 경우
				 * or
				 * 통계 쿠키가 발급된 적이 없는 경우
				 */
				if ((cookie.getValue().contains("statistic") && !cookie.getValue()
					.contains(request.getContextPath())) || !cookie.getValue()
					.contains("statistic")) { // 통계 쿠키가 있지만 방문한 적 없을 경우

					createStatCookie(request, response, cookie);

					totalStatistics.addTotalVisitorCount();
				}
			}
		} else if (cookies == null) { // 쿠키 자체가 없는 경우 새로 발급

			String key = "visit_cookie";
			String value =
				"statistic" + "_" + "[" + LocalDateTime.now() + "]" + "_" + "[" + request.getContextPath()
					+ "]";

			ResponseCookie newStatCookie = ResponseCookie.from(key, value)
				.maxAge(2 * 60 * 60) // 두 시간
				.path("/")
				.secure(true)
				.sameSite("None")
				.httpOnly(true)
				.build();

			response.setHeader("Set-Cookie", newStatCookie.toString());

			totalStatistics.addTotalVisitorCount();
		}

		totalStatisticsRepository.save(totalStatistics);

	}

	@Before(value = "Pointcuts.getCoffee() && args(coffeeId)")
	public void calculateStaticticsOnGetCoffee(JoinPoint joinPoint, Long coffeeId) {
		TotalStatistics totalStatistics = totalStatisticsQueryRepository.findTotalStatistics();
		totalStatistics.addTotalCoffeeViewCount();
		totalStatisticsRepository.save(totalStatistics);
	}

	/*
	 * 레이팅 새로 등록될 때마다 인기 태그, 베스트 레이팅, 평균 별점 계산 후 변경 사항 저장
	 */
	@AfterReturning(value = "Pointcuts.createRating() && args(rating, coffeeId, ratingTag)")
	public void calculateCoffeeDetailsOnCreation(JoinPoint joinPoint, Rating rating, Long coffeeId,
		RatingTag ratingTag) {

		TotalStatistics totalStatistics = totalStatisticsQueryRepository.findTotalStatistics();
		totalStatistics.addTotalRatingCount();

		Coffee findCoffee = coffeeService.findVerifiedCoffee(coffeeId);

		findCoffee.addStatRatingCount(); // 맥주 통계용 레이팅 숫자 늘려주기
		// findCoffee.addRatingCount(); // 맥주 전체 레이팅 숫자 늘려주기

		// ------------------------------------BEER TAG-------------------------------------------

		List<CoffeeTagType> postCoffeeTagTypes = ratingTag.createCoffeeTagTypeList(); // 입력받은 맥주 태그 타입

		postCoffeeTagTypes.forEach(coffeeTagType -> { // 태그 카운트 늘려주기
			CoffeeTag findCoffeeTag = coffeeTagService.findVerifiedCoffeeTagByCoffeeTagType(coffeeTagType);
			findCoffeeTag.addDailyCount();
		});

		List<CoffeeTag> coffeeTags = coffeeService.findTop4CoffeeTags(findCoffee); // 상위 태그 새로 계산하기

		List<String> coffeeTagTypes = coffeeTags.stream() // 입력받은 맥주 태그 타입 문자열 리스트로 전환
			.map(coffeeTag -> coffeeTag.getCoffeeTagType().toString())
			.collect(Collectors.toList());

		if (findCoffee.getCoffeeDetailsTopTags() == null) {
			findCoffee.createTopTags(coffeeTagTypes);
		} else {
			List<String> presentCoffeeTagTypes = findCoffee.createTopTagList(); // 기존 상위 태그
			if (presentCoffeeTagTypes != coffeeTagTypes) { // 둘이 다르면 교체
				findCoffee.getCoffeeDetailsTopTags().changeTags(coffeeTagTypes);
			}
		}

		// ---------------------------------------------------------------------------------------
		// ----------------------------------BEST RATING------------------------------------------

		if (findCoffee.getCoffeeDetailsBestRating() == null
			|| findCoffee.getCoffeeDetailsBestRating().getBestLikeCount() == 0 &&
			rating.getStar() >= findCoffee.getCoffeeDetailsBestRating().getBestStar()) {
			findCoffee.updateBestRating(rating);
		}
		// ---------------------------------------------------------------------------------------
		// --------------------------------BEER DETAIL STARS--------------------------------------

		User loginUser = userService.getLoginUser();

		findCoffee.calculateTotalAverageStars(rating.getStar());

		if (loginUser.getGender() == Gender.FEMALE) {
			findCoffee.calculateFemaleAverageStars(rating.getStar());
			// findCoffee.addFemaleStarCount();
		} else if (loginUser.getGender() == Gender.MALE) {
			findCoffee.calculateMaleAverageStars(rating.getStar());
			// findCoffee.addMaleStarCount();
		}
		// ---------------------------------------------------------------------------------------
		totalStatisticsRepository.save(totalStatistics);
		coffeeRepository.save(findCoffee);
	}

	/*
	 * 레이팅 수정시 인기 태그, 평균 별점 계산 후 변경 사항 저장
	 */
	@Before(value = "Pointcuts.updateRating() && args(rating, ratingId, ratingTag)")
	public void calculateCoffeeDetailsOnUpdate(JoinPoint joinPoint, Rating rating, Long ratingId,
		RatingTag ratingTag) {

		Coffee findCoffee = coffeeService.findCoffeeByRatingId(ratingId);

		// ------------------------------------BEER TAG-------------------------------------------

		List<CoffeeTagType> postCoffeeTagTypes = ratingTag.createCoffeeTagTypeList(); // 입력받은 맥주 태그 타입

		postCoffeeTagTypes.forEach(coffeeTagType -> { // 태그 카운트 늘려주기
			CoffeeTag findCoffeeTag = coffeeTagService.findVerifiedCoffeeTagByCoffeeTagType(coffeeTagType);
			findCoffeeTag.addDailyCount();
		});

		List<CoffeeTag> coffeeTags = coffeeService.findTop4CoffeeTags(findCoffee); // 상위 태그 새로 계산

		List<String> coffeeTagTypes = coffeeTags.stream() // 입력받은 맥주 태그 타입 문자열 리스트로 전환
			.map(coffeeTag -> coffeeTag.getCoffeeTagType().toString())
			.collect(Collectors.toList());

		if (findCoffee.getCoffeeDetailsTopTags() == null) {
			findCoffee.createTopTags(coffeeTagTypes);
		} else {
			List<String> presentCoffeeTagTypes = findCoffee.createTopTagList(); // 기존 상위 태그
			if (presentCoffeeTagTypes != coffeeTagTypes) { // 둘이 다르면 교체
				findCoffee.getCoffeeDetailsTopTags().changeTags(coffeeTagTypes);
			}
		}

		// ---------------------------------------------------------------------------------------
		// --------------------------------BEER DETAIL STARS--------------------------------------

		User loginUser = userService.getLoginUser();
		Rating previousRating = ratingService.findRating(ratingId); // 메서드 실행 전 레이팅
		Double previousStar = previousRating.getStar();
		Double afterStar = rating.getStar();

		findCoffee.updateTotalAverageStars(previousStar, afterStar);

		if (loginUser.getGender().equals(Gender.FEMALE)) {
			findCoffee.updateFemaleAverageStars(previousStar, afterStar);
		} else if (loginUser.getGender().equals(Gender.MALE)) {
			findCoffee.updateMaleAverageStars(previousStar, afterStar);
		}

		// ---------------------------------------------------------------------------------------

		coffeeRepository.save(findCoffee);
	}

	@Around(value = "Pointcuts.deleteRating() && args(ratingId)")
	public Object calculateCoffeeDetailsOnDeletion(ProceedingJoinPoint joinPoint, Long ratingId) throws Throwable {

		Coffee findCoffee = coffeeService.findCoffeeByRatingId(ratingId);
		User loginUser = userService.getLoginUser();

		try {
			// @Before
			Rating findRating = ratingService.findRating(ratingId);
			Double deleteStar = findRating.getStar();

			List<CoffeeTagType> coffeeTagTypeList = findRating.getRatingTag().createCoffeeTagTypeList();

			findCoffee.getCoffeeCoffeeTags().stream()
				.filter(coffeeCoffeeTag -> coffeeTagTypeList.contains(coffeeCoffeeTag.getCoffeeTag().getCoffeeTagType()))
				.map(CoffeeCoffeeTag::getCoffeeTag)
				.forEach(coffeeTag -> {
					coffeeTag.subtractDailyCount();
					coffeeTagRepository.save(coffeeTag);
				});

			findCoffee.deleteTotalAverageStars(deleteStar);
			findCoffee.minusRatingCount();

			if (loginUser.getGender().equals(Gender.FEMALE)) {
				findCoffee.deleteFemaleAverageStars(deleteStar);
			} else if (loginUser.getGender().equals(Gender.MALE)) {
				findCoffee.deleteMaleAverageStars(deleteStar);
			}

			coffeeRepository.save(findCoffee);

			List<String> presentCoffeeTagTypes = new ArrayList<>();

			if (findCoffee.createTopTagList() != null) {
				presentCoffeeTagTypes = findCoffee.createTopTagList(); // 삭제 이전 베스트 태그
			}

			log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
			Object result = joinPoint.proceed();

			// @AfterReturning
			List<CoffeeTag> coffeeTags = coffeeService.findTop4CoffeeTags(findCoffee); // 상위 태그 새로 계산

			List<String> coffeeTagTypes = coffeeTags.stream() // 맥주 태그 타입 문자열 리스트로 전환
				.map(coffeeTag -> coffeeTag.getCoffeeTagType().toString())
				.collect(Collectors.toList());

			if (presentCoffeeTagTypes != coffeeTagTypes && coffeeTagTypes.size() != 0) { // 둘이 다르면 교체
				findCoffee.getCoffeeDetailsTopTags().changeTags(coffeeTagTypes);
			} else if (presentCoffeeTagTypes != coffeeTagTypes && coffeeTagTypes.size() == 0) {
				findCoffee.makeTopTagsNull();
			}

			// 삭제되는 레이팅이 베스트 레이팅일 경우
			if (findCoffee.getCoffeeDetailsBestRating().getBestRatingId() == ratingId
				&& coffeeService.findBestRating(findCoffee) != null) {
				Rating bestRating = coffeeService.findBestRating(findCoffee);
				findCoffee.updateBestRating(bestRating);
			} else if (findCoffee.getCoffeeDetailsBestRating().getBestRatingId() == ratingId
				&& coffeeService.findBestRating(findCoffee) == null) {
				findCoffee.deleteCoffeeDetailsBestRating();
			}

			coffeeRepository.save(findCoffee);

			log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());

			return result;
		} catch (Exception e) {
			// @AfterThrowing
			log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
			throw e;
		} finally {
			// @After
			log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
		}

	}

	@AfterReturning(value = "Pointcuts.createPairing() && args(pairing, files, coffeeId)")
	public void calculateCoffeeDetailsOnPairingCreation(JoinPoint joinPoint, Pairing pairing, List<MultipartFile> files,
		Long coffeeId) {

		TotalStatistics totalStatistics = totalStatisticsQueryRepository.findTotalStatistics();
		totalStatistics.addTotalPairingCount();

		// ---------------------------------------------------------------------------------------
		// TODO: 페어링 생성시 각종 계산
		// ---------------------------------------------------------------------------------------

		Coffee findCoffee = coffeeService.findVerifiedCoffee(coffeeId);

		String bestPairingCategory = coffeeService.findBestPairingCategory(findCoffee);

		if (findCoffee.getCoffeeDetailsStatistics().getBestPairingCategory() != bestPairingCategory) {
			findCoffee.getCoffeeDetailsStatistics().updateBestPairingCategory(bestPairingCategory);
		}

		totalStatisticsRepository.save(totalStatistics);
		coffeeRepository.save(findCoffee);

	}

	@AfterReturning(value = "Pointcuts.updatePairing() && args(pairing, pairingId, category, image)")
	public void calculateCoffeeDetailsOnPairingUpdate(JoinPoint joinPoint, Pairing pairing, Long pairingId,
		String category, List<String> image) {

		// ---------------------------------------------------------------------------------------
		// TODO: 페어링 수정시 각종 계산
		// ---------------------------------------------------------------------------------------

		Coffee findCoffee = coffeeService.findCoffeeByPairingId(pairingId);

		String bestPairingCategory = coffeeService.findBestPairingCategory(findCoffee);

		if (findCoffee.getCoffeeDetailsStatistics().getBestPairingCategory() != bestPairingCategory) {
			findCoffee.getCoffeeDetailsStatistics().updateBestPairingCategory(bestPairingCategory);
		}

		coffeeRepository.save(findCoffee);
	}

	@Before(value = "Pointcuts.deletePairing() && args(pairingId)")
	public void calculateCoffeeDetailsOnPairingDeletion(JoinPoint joinPoint, Long pairingId) {

		// ---------------------------------------------------------------------------------------
		// TODO: 페어링 삭제시 각종 계산
		// ---------------------------------------------------------------------------------------

		Coffee findCoffee = coffeeService.findCoffeeByPairingId(pairingId);

		String bestPairingCategory;

		if (coffeeService.findBestPairingCategory(findCoffee) != null) {
			bestPairingCategory = coffeeService.findBestPairingCategory(findCoffee);
		} else {
			bestPairingCategory = null;
		}

		if (findCoffee.getCoffeeDetailsStatistics().getBestPairingCategory() != bestPairingCategory) {
			findCoffee.getCoffeeDetailsStatistics().updateBestPairingCategory(bestPairingCategory);
		}

		coffeeRepository.save(findCoffee);

	}

	@Before(value = "Pointcuts.getCoffee() && args(coffeeId)")
	public void test(JoinPoint joinPoint, Long coffeeId) {
		Coffee findCoffee = coffeeService.findVerifiedCoffee(coffeeId);
		findCoffee.addStatViewCount();
		findCoffee.getCoffeeCoffeeCategories().stream()
			.map(CoffeeCoffeeCategory::getCoffeeCategory)
			.forEach(coffeeCategory -> {
				coffeeCategory.addStatCount();
				coffeeCategoryRepository.save(coffeeCategory);
			});
		coffeeRepository.save(findCoffee);
	}

	@AfterReturning(value = "Pointcuts.clickRatingLike() && args(ratingId)")
	public void calculateBestPairingOnPairingLike(JoinPoint joinPoint, Long ratingId) {

		Rating findRating = ratingService.findRating(ratingId);
		Coffee findCoffee = coffeeService.findCoffeeByRatingId(ratingId);

		if (findCoffee.getCoffeeDetailsBestRating() != null) {
			Long bestRatingId = findCoffee.getCoffeeDetailsBestRating().getBestRatingId();
			Integer bestRatingStarCount = ratingService.findRating(bestRatingId).getLikeCount();

			if (findRating.getId().equals(bestRatingId)
				|| findRating.getLikeCount() > bestRatingStarCount) { // 좋아요 찍힌게 베스트 레이팅이거나 순위가 바뀌면 새로 계산
				Rating bestRating = coffeeService.findBestRating(findCoffee);
				findCoffee.updateBestRating(bestRating);
			}
		}
		coffeeRepository.save(findCoffee);
	}

	@Before(value = "Pointcuts.updateUser() && args(edit)")
	public void calculateStarsOnUpdateUser(JoinPoint joinPoint, User edit) {

		User loginUser = userService.getLoginUser();
		Gender loginUserGender = loginUser.getGender();
		Gender updatedUserGender = edit.getGender();

		List<Coffee> coffeeList = coffeeQueryRepository.findRatedCoffeesListByUserId(loginUser.getId());

		coffeeList.stream()
			.map(coffee -> {

				Double star = coffee.getRatingList().stream()
					.filter(rating -> rating.getUser().getId().equals(loginUser.getId()))
					.findFirst().get().getStar();

				if (loginUserGender == Gender.REFUSE) {
					if (updatedUserGender == Gender.FEMALE) {
						coffee.calculateFemaleAverageStars(star);
					} else if (updatedUserGender == Gender.MALE) {
						coffee.calculateMaleAverageStars(star);
					}
				} else if (loginUserGender == Gender.FEMALE) {
					if (updatedUserGender == Gender.REFUSE) {
						coffee.deleteFemaleAverageStars(star);
					} else if (updatedUserGender == Gender.MALE) {
						coffee.deleteFemaleAverageStars(star);
						coffee.calculateMaleAverageStars(star);
					}
				} else if (loginUserGender == Gender.MALE) {
					if (updatedUserGender == Gender.REFUSE) {
						coffee.deleteMaleAverageStars(star);
					} else if (updatedUserGender == Gender.FEMALE) {
						coffee.deleteMaleAverageStars(star);
						coffee.calculateFemaleAverageStars(star);
					}
				}
				return coffeeRepository.save(coffee);
			}).collect(Collectors.toList());
	}

	private void createStatCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {

		String key = "visit_cookie";
		String value =
			"statistic" + "_" + "[" + LocalDateTime.now() + "]" + "_" + "[" + request.getContextPath()
				+ "]";

		ResponseCookie newStatCookie = ResponseCookie.from(key, value)
			.maxAge(2 * 60 * 60) // 두 시간
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();

		cookie.setMaxAge(0);

		response.setHeader("Set-Cookie", newStatCookie.toString());
	}

	@WebListener
	public static class sessionStatistics implements HttpSessionListener { // 세션 사용하게 되면 쓰기

		@Override
		public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		}

		@Override
		public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		}
	}
}
