# IntelliJ 저장소 동기화
## IntelliJ에서 Get from VCS를 누른다.(프로젝트가 열려있다면 File에서 Close Project)
![1 GetFromVCS](https://user-images.githubusercontent.com/76415175/112301575-6d0a8880-8cdd-11eb-9a1c-323c07793b00.PNG)
  
## 자신의 깃허브계정에서 Clone해올 Repository를 선택한다.
(하단Directory는 Clone해올 Repository의 저장경로)  
![2 Clone해올레파지토리](https://user-images.githubusercontent.com/76415175/112301630-7d226800-8cdd-11eb-8565-5efd2790a48e.PNG)
  
## Clone해왔을때 트리 상태
![2 트리상태](https://user-images.githubusercontent.com/76415175/112301688-8ad7ed80-8cdd-11eb-99e8-5714508351ad.PNG)
  
## 프로젝트를 우클릭후 Git에 Merge Remotes...를 눌러 Repository를 가져온다.
(자신의 저장소가 아님)  
![3 remote](https://user-images.githubusercontent.com/76415175/112301740-9a573680-8cdd-11eb-8704-c79d0b4607ce.PNG)
  
## 그후 remote해온걸 클릭후 OK버튼을 클릭한다.
![3 remote후 선택](https://user-images.githubusercontent.com/76415175/112301756-9d522700-8cdd-11eb-98a5-28b41bb4916b.PNG)
  
## 그후 프로젝트를 우클릭후 Git에 Fetch를 누른다.
(Fetch후 트리상태)  
![4 fetch후 트리상태](https://user-images.githubusercontent.com/76415175/112301784-a3e09e80-8cdd-11eb-885d-97252cac285e.PNG)
  
## rebase를할 branch를 생성한다.
(브랜치를 생선안한다면 rebase할 브랜치를 선택후 rebase한다)  
![5 브랜치생성](https://user-images.githubusercontent.com/76415175/112301801-a8a55280-8cdd-11eb-92af-11f2d71c0048.PNG)
  
## 그후 프로젝트를 우클릭후 Git에 rebase를 눌러 병합할 저장소의 브랜치를 선택한다.
![6 rebase](https://user-images.githubusercontent.com/76415175/112301820-ad6a0680-8cdd-11eb-81e9-914ee83d80e3.PNG)
  
## rebase후 선택되어있던 브랜치 상태
![7 rebase후 트리상태](https://user-images.githubusercontent.com/76415175/112301834-b1962400-8cdd-11eb-8189-54fa0abe0fab.PNG)

## 그후 작업을 시작하면 된다. 저장소의 상태가 변경되었다면 위작업을 다시 하면된다.
