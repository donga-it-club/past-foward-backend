package aws.retrospective.entity;




import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.FetchType;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

import java.util.List;

import java.util.UUID;

import lombok.AccessLevel;

import lombok.Builder;

import lombok.Getter;

import lombok.NoArgsConstructor;




@Entity

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class RetrospectiveGroup extends BaseEntity {




    @Id

    @Column(name = "retrospectiveGroup_id")

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; //회고 그룹 ID




    @NotNull

    private String title; //회고 그룹 제목




    private UUID thumbnail; //회고 그룹 썸네일




    private String description; //회고 그룹 설명




    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id")

    @NotNull

    private User user; //회고를 작성한 사용자 정보




    @Enumerated(value = EnumType.STRING)

    private ProjectStatus status; //회고 진행 상태(진행 중, 완료)




    @OneToMany(mappedBy = "retrospectiveGroup")

    private List<Bookmark> bookmarks = new ArrayList<>();




    @OneToMany(mappedBy = "retrospectiveGroup", cascade = CascadeType.REMOVE)

    private List<Retrospective> retrospectives = new ArrayList<>();




    @Builder

    public RetrospectiveGroup(

            String title,

            UUID thumbnail,

            String description,

            User user,

            ProjectStatus status

    ) {

        this.title = title;

        this.thumbnail = thumbnail;

        this.description = description;

        this.user = user;

        this.status = status;

    }




    public void update(String title, ProjectStatus status, UUID thumbnail, String description) {

        this.title = title;

        this.status = status;

        this.thumbnail = thumbnail;

        this.description = description;

    }

    public void addRetrospective(Retrospective retrospective) {

        retrospectives.add(retrospective);

        retrospective.setRetrospectiveGroup(this);

    }




    public void removeRetrospective(Retrospective retrospective) {

        retrospectives.remove(retrospective);

        retrospective.setRetrospectiveGroup(null);

    }




    public boolean isOwnedByUser(Long userId) {

        return this.user.getId().equals(userId);

    }

}