package be.domain.user.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.entity.CoffeeCategoryType;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;
import be.domain.follow.repository.FollowQueryRepository;
import be.domain.user.dto.UserDto;
import be.domain.user.entity.User;
import be.domain.user.entity.UserCoffeeCategory;
import be.domain.user.entity.UserCoffeeTag;

@Mapper(componentModel = "spring")
public interface UserMapper {
	default User postToUser(UserDto.RegisterPost post) {
		return User.builder()
			.email(post.getEmail())
			.password(post.getPassword())
			.nickname(post.getNickname())
			.build();
	}

	default UserDto.LoginResponse userToLoginResponse(User user) {
		return UserDto.LoginResponse.builder()
			.id(user.getId())
			.email(user.getEmail())
			.nickname(user.getNickname())
			.build();
	}

	default UserDto.UserInfoResponse userToInfoResponse(User user) {
		List<String> userCoffeeTags = user.getUserCoffeeTags().stream().map(
			userCoffeeTag -> userCoffeeTag.getCoffeeTag().getCoffeeTagType().toString()
		).collect(Collectors.toList());

		List<String> userCoffeeCategories = user.getUserCoffeeCategories().stream().map(
			userCoffeeCategory -> userCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType().toString()
		).collect(Collectors.toList());

		return UserDto.UserInfoResponse.builder()
			.imageUrl(user.getImageUrl())
			.nickname(user.getNickname())
			.age(user.getAge())
			.gender(user.getGender())
			.followerCount(user.getFollowerCount())
			.followingCount(user.getFollowingCount())
			.userCoffeeTags(userCoffeeTags)
			.userCoffeeCategories(userCoffeeCategories)
			.build();
	}

	default User infoPostToUser(Long id, UserDto.UserInfoPost postInfo) {
		User user = new User();
		user.putId(id);
		user.putUserInfo(postInfo.getAge(), postInfo.getGender());

		List<UserCoffeeTag> userCoffeeTags = getUserCoffeeTag(postInfo.getUserCoffeeTags());
		user.putUserCoffeeTags(userCoffeeTags);

		List<UserCoffeeCategory> userCoffeeCategories = getUserCoffeeCategory(postInfo.getUserCoffeeCategories());
		user.putUserCoffeeCategories(userCoffeeCategories);

		return user;
	}

	default User editToUser(UserDto.EditUserInfo edit) {
		User user = new User();
		user.edit(edit.getImageUrl(), edit.getNickname(), edit.getGender(), edit.getAge());

		if (edit.getUserCoffeeTags() != null) {
			List<UserCoffeeTag> userCoffeeTags = getUserCoffeeTag(edit.getUserCoffeeTags());
			user.putUserCoffeeTags(userCoffeeTags);
		}

		if (edit.getUserCoffeeCategories() != null) {
			List<UserCoffeeCategory> userCoffeeCategories = getUserCoffeeCategory(edit.getUserCoffeeCategories());
			user.putUserCoffeeCategories(userCoffeeCategories);
		}

		return user;
	}

	default PageImpl<UserDto.UserSearchResponse> userToUserSearchResponses(
		Page<User> userPage, FollowQueryRepository followQueryRepository, Long followingUserId) {

		return new PageImpl<>(userPage.stream().map(user -> {

			UserDto.UserSearchResponse.UserSearchResponseBuilder userSearchResponseBuilder = UserDto.UserSearchResponse.builder();

			userSearchResponseBuilder.userId(user.getId());
			userSearchResponseBuilder.nickname(user.getNickname());
			userSearchResponseBuilder.imageUrl(user.getImageUrl());
			userSearchResponseBuilder
				.isFollowing(followQueryRepository.findFollowByUserIds(followingUserId, user.getId()) != null);

			return userSearchResponseBuilder.build();

		}).collect(Collectors.toList()));
	}

	default PageImpl<UserDto.UserSearchResponse> userToUserSearchResponses(
		Page<User> userPage, FollowQueryRepository followQueryRepository) {

		return new PageImpl<>(userPage.stream().map(user -> {

			UserDto.UserSearchResponse.UserSearchResponseBuilder userSearchResponseBuilder = UserDto.UserSearchResponse.builder();

			userSearchResponseBuilder.userId(user.getId());
			userSearchResponseBuilder.nickname(user.getNickname());
			userSearchResponseBuilder.imageUrl(user.getImageUrl());
			userSearchResponseBuilder.isFollowing(false);

			return userSearchResponseBuilder.build();

		}).collect(Collectors.toList()));
	}

	private static List<UserCoffeeTag> getUserCoffeeTag(List<String> responses) {
		return responses.stream().map(
			response -> UserCoffeeTag.builder()
				.coffeeTag(CoffeeTag.builder()
					// .id()
					.coffeeTagType(CoffeeTagType.valueOf(response))
					.build())
				.build()).collect(Collectors.toList());
	}

	private static List<UserCoffeeCategory> getUserCoffeeCategory(List<String> responses) {
		return responses.stream().map(
			response -> UserCoffeeCategory.builder()
				.coffeeCategory(CoffeeCategory.builder()
					// .id(response.getCoffeeCategoryId())
					.coffeeCategoryType(CoffeeCategoryType.valueOf(response))
					.build())
				.build()).collect(Collectors.toList());
	}
}
