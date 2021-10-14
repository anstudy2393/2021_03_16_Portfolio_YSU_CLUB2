#오류들 해결법
##1.동아리 신청 내역이 있을경우
![스크린샷 2021-04-10 오후 1 25 55](https://user-images.githubusercontent.com/61412496/114258256-50ba5b80-9a00-11eb-9abe-2b0c3b0e1de4.png)
## 동아리를 삭제하게 되면 오류가 발생한다.
![스크린샷 2021-04-10 오후 1 30 34](https://user-images.githubusercontent.com/61412496/114258379-10a7a880-9a01-11eb-8e64-f83cc768f03c.png)
### 이유는 참조 무결성제약 조건 때문이다.
### 여기에 자세하게 나와있다. 
https://velog.io/@woodyn1002/삽질-로그-Hibernate에서-부모가-둘인-Entity의-한쪽-부모를-지우면-참조-무결성-오류가-발생하는-문제

#### club의 내용을 바꿔주면 오류가 해결된다.
```java
    @OneToMany(mappedBy = "club",cascade = CascadeType.REMOVE)
    private List<JoinClub> joinClubs = new ArrayList<>();
```

### 삭제 하려니깐 인덱스 오류가 나온다.
![스크린샷 2021-04-28 오후 10 02 26](https://user-images.githubusercontent.com/61412496/116410520-d39c3c80-a86f-11eb-9f05-5a06c3014428.png)
### 이유
+ 찾아온 리스트의 사이즈만 큼 반복을 할 때 삭제할수록 리스트의 크기도 바뀌기때문에 인덱스가 꼬여버린다.
```java
for(int i=0; i<=byClub.size();i++) {
log.info("나와라요~~~" + byClub.get(i).getJoin_Id());
Join join = joinRepository.findOne(byClub.get(i).getJoin_Id());
join.cancel();
}
```
### 해결법
+ 찾아온 리스트의 사이즈를 변수로 저장해서 정해진 사이즈만 큼 반복한다.
```java
     @Transactional
public void chageStatus(Club club){
        Long clubId = club.getId();
        log.info("갑이야갑ㄱ박박바갑갑가박ㅂ가"+clubId);
        List<JoinclubMapping> byClub = joinClubRepository.findByClub(club);
        int size = byClub.size();
        for(int i=0; i<size;i++) {
        log.info("나와라요~~~" + byClub.get(i).getJoin_Id());
        Join join = joinRepository.findOne(byClub.get(i).getJoin_Id());
        join.cancel();
        }
```