package be.domain.rating.dto;

import java.time.LocalDateTime;
import java.util.List;

import be.domain.coffeetag.entity.CoffeeTagType;
import be.domain.comment.dto.RatingCommentDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RatingResponseDto {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Detail {
		public Detail() {
		}

		private Long coffeeId;
		private String korName;
		private Long ratingId;
		private Long userId;
		private String nickname;
		private String userImage;
		private String content;
		private List<CoffeeTagType> ratingTag;
		private Double star;
		private Integer likeCount;
		private Integer commentCount;
		private List<RatingCommentDto.Response> ratingCommentList;
		private Boolean isUserLikes;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;

		public void addTag(List<CoffeeTagType> ratingTag) {
			this.ratingTag = ratingTag;
		}

		public void addComment(List<RatingCommentDto.Response> ratingCommentList) {
			this.ratingCommentList = ratingCommentList;
		}

		public void addUserLike(Boolean isUserLikes) {
			this.isUserLikes = isUserLikes;
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Total {
		public Total() {
		}

		private Long coffeeId;
		private String korName;
		private Long ratingId;
		private Long userId;
		private String nickname;
		private String userImage;
		private String content;
		private List<CoffeeTagType> ratingTag;
		private Double star;
		private Integer likeCount;
		private Integer commentCount;
		private Boolean isUserLikes;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;

		public void addTag(List<CoffeeTagType> ratingTag) {
			this.ratingTag = ratingTag;
		}

		public void addUserLike(Boolean isUserLikes) {
			this.isUserLikes = isUserLikes;
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MyPageResponse {
		private Long coffeeId;
		private Long ratingId;
		private Long userId;
		private String nickname;
		private String userImage;
		private String content;
		private List<CoffeeTagType> ratingTag;
		private Double star;
		private Integer likeCount;
		private Integer commentCount;
		private Boolean isUserLikes;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class UserPageResponse {
		private Long coffeeId;
		private Long ratingId;
		private Long userId;
		private String nickname;
		private String userImage;
		private String content;
		private List<CoffeeTagType> ratingTag;
		private Double star;
		private Integer likeCount;
		private Integer commentCount;
		private Boolean isUserLikes;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}
}
