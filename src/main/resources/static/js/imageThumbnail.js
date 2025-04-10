// addEventListener 기반으로 챗지피티 코드 생성

export function initImageThumbnailUpload({ inputSelector, imgSelector, defaultImage }) {
    const inputEl = document.querySelector(inputSelector);
    const imgEl = document.querySelector(imgSelector);

    // 파일 선택 이벤트 리스너 등록
    inputEl.addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = function (e) {
            imgEl.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });

    // 외부에서 기본 이미지로 초기화할 수 있도록 reset 함수 반환
    function resetImage() {
        inputEl.value = '';              // input 비우기
        imgEl.src = defaultImage || '';  // 기본 이미지로 변경
    }

    // 외부에서 접근할 수 있도록 객체 반환
    return {
        resetImage
    };
}
