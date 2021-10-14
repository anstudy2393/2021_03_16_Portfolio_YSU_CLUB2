## 1주차(Github 협업)

# 2021_03_09_GitHub_Study
<h1>Git Bash Study</h1>
<ul><h2>계정 정보 등록</h2>
	<li>email등록 : git config --global user.email "계정 email"</li>
	<li>계정 등록 : git congfig --global user.name "계정 username"</li>
</ul>
<ul><h2>계정 정보 확인</h2>
	<li>email확인 : git config user.email</li>
	<li>계정확인 : git congfig user.name</li>
</ul>
<ul><h2>도움말(help)</h2>
	<li>도움말 : git help</li>
	<li>특정 명령어 도움말 : git help {명령어}</li>
</ul>
<ul><h2>init</h2>
	<li>해당 폴더를 로컬 저장소로 지정 : git init</li>
</ul>
<ul><h2>Clone</h2>
	<li>해당 저장소를 clone : git clone {git address}</li>
	<li>특정 브랜치만 clone : git clone -b{branch_name} --single-branch {git address}</li>
</ul>
<ul><h2>status</h2>
	<li>워킹트리의 상태확인 : git status</li>
	<li>간결하게 상태확인 : git status -s</li>
</ul>
<ul><h2>add</h2>
	<li>파일 스테이지에 추가 : git add file1 file2...</li>
	<li>변경된 모든 파일 추가 : git add .</li>
</ul>
<ul><h2>reset</h2>
	<li>스테이지에 올린 파일을 스테이지에서 내림 : git reset file1</li>
	<li>현제 브랜치를 지정한 커밋으로 옮김, 작업내용도 함께 변경 : git reset --hard {이동할 커밋 체크섬}
		<ul>
			<li>ex : git reset --hard HEAD~2 (두커밋 이전으로 이동) </li>
		</ul>
	</li>
</ul>
<ul><h2>commit</h2>
	<li>스테이지에 있는 파일 커밋 : git commit</li>
	<li>add를 생략하고 커밋 : git commit -a</li>
	<li>커밋시 메시지 작성 : git commit -m "Message"</li>
</ul>
<ul><h2>log</h2>
	<li>커밋 히스토리 확인 : git log</li>
	<li>최신 n개만 확인 : git log -n{숫자}</li>
	<li>간결하게 확인 : git log --oneline --graph --decorate --all
		<ul>
			<li>--oneline : 커밋 메시지를 한줄로 요약해서 보여줌</li>
			<li>--graph : 커밋 옆애 브랜치의 흐름을 그래프로 보여줌</li>
			<li>--decorate : 브랜치와 태그 등의 참조를 간결히표시</li>
			<li>--all : all 옵션이 없을 경우 HEAD와 관계없는 옵션은 보여주지 않음</li>
		</ul>
	</li>
</ul>
<ul><h2>push</h2>
	<li>원격저장소 등록 : git remote add {원격 저장소 이름} {원격 저장소 주소}</li>
	<li>원격저장소 목록확인 : git remote -v</li>
</ul>
<ul><h2>branch</h2>
	<li>브랜치목록확인 : git branch -v</li>
	<li>브랜치 이동 : git checkout {브랜치 이름}</li>
	<li>브랜치 생성/이동 : git checkout -b {브랜치 이름}</li>
	<li>브랜치 병합 : git merge {대상 브랜치}</li>
	<li>브랜치 재배치 : git rebase {대상 브랜치}</li>
	<li>브랜치 삭제(HEAD브랜치나 병합되지 않은 브랜치는 삭제X) : git branch -d</li>
	<li>브랜치 강제삭제 : git branch -D</li>
</ul>
<ul><h2>tag</h2>
	<li>태그 생성 : git tag -a -m "태그이름" v0.1</li>
	<li>태그 생성 확인 : git log --oneline</li>
	<li>태그 push : git push origin v0.1</li>
</ul>

<h3>사용 프로그램 : Git Bash</h3>
<h3>IntelliJ GitBash연동</h3>
<pre>
1. IntelliJ에서 ctrl + alt + s 키를 눌러 Settings창을 연다. 또는 File에 Settings를 찾아 연다.
2. Tools -> Terminal을 눌러 Shell path가 cmd.exe로 되어있는걸 확인 할수 있음.
3. Shell path를 "Git/bin/sh.exe" -login -i로 변경한다.(경로는 사용자마다 다를 수 있음)
4. 그후 Terminal을 실행해보면 GitBash로 바뀐걸 확인할수 있다.
</pre>
<h3>병합 충돌</h3>
<h5>main branch의 Test.txt</h5>
<pre>
테스트456
789
</pre>
<h5>Sangnam branch의 Test.txt</h5>
<pre>
테스트789
123
</pre>
<img src="https://user-images.githubusercontent.com/76415175/110462159-ac4cad00-8113-11eb-95eb-48097e1fcd06.PNG">
<p>같은 줄의 내용이 수정이되니 merge를 하려니 충돌이 난다.</p>
<pre>
<<<<<<< HEAD (현재 변경 사항)
테스트456
789
======
테스트789
123
>>>>>>> Sangnam (수신 변경 사항)
</pre>
<p>둘중 하나를 선택하거나 변경사항을 고친다.</p>
<img src="https://user-images.githubusercontent.com/76415175/110462163-ace54380-8113-11eb-816c-626b045e090a.PNG">
<p>그후 git status로 상태를 보면 Test.txt에 붉은 표시가 된걸 볼수있다.</p>
<img src="https://user-images.githubusercontent.com/76415175/110462165-ace54380-8113-11eb-96d1-fb5f8ce261c9.PNG">
<p>add후 status상태를 보면 녹색표시로 바뀐걸 볼수있다.</p>
<img src="https://user-images.githubusercontent.com/76415175/110462153-ab1b8000-8113-11eb-9cf4-a55e7ae01e2f.PNG">
<p>그후 commit후 push를 해보면 바뀐걸 확인할수 있다.</p>
<p>충돌 해결후 main에 Test.txt</p>
<img src="https://user-images.githubusercontent.com/76415175/110462155-abb41680-8113-11eb-8d32-ddf5f09d6091.PNG">
<p>충돌 해결후 Sangnam에 Test.txt</p>
<img src="https://user-images.githubusercontent.com/76415175/110462157-ac4cad00-8113-11eb-8799-58639c5814e3.PNG">
<p>수정한 대로 병합된걸 확인할수 있다.</p>
