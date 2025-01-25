// 삭제
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', async () => {
        try {
            // 게시글 ID 확인
            const postIdInput = document.getElementById('post-id');
            const id = postIdInput ? postIdInput.value : null;

            if (!id) {
                alert('게시글 ID를 찾을 수 없습니다.');
                return;
            }

            // DELETE 요청 보내기
            const response = await fetch(`/api/post/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log(`응답 상태 코드: ${response.status}`); // 디버깅용 로그

            if (response.ok) {
                alert('삭제가 완료되었습니다.');
                location.replace('/post'); // 목록 페이지로 리다이렉트
            } else {
                const errorText = await response.text();
                console.error('DELETE 요청 실패:', errorText);
                alert(`삭제 실패: ${errorText}`);
            }
        } catch (error) {
            console.error('DELETE 요청 중 오류:', error);
            alert('삭제 요청 중 문제가 발생했습니다. 다시 시도해주세요.');
        }
    });
} else {
    console.error("'delete-btn' 요소를 찾을 수 없습니다.");
}

// 수정
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/post/${id}`, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/post/${id}`)
            })
    });
} else {
    console.error("'modify-btn' 요소를 찾을 수 없습니다.");
}


// 등록
const createButton = document.getElementById("create-btn")
if (createButton) {
    createButton.addEventListener("click", event => {
        fetch("/api/post", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() =>{
                alert("등록 완료되었습니다.");
                location.replace("/post");
            });
    });
}
