export function initMultiImageUpload({ 
    inputSelector, 
    previewRowSelector, 
    addButtonSelector, 
    maxCount = 3 
}) {
    const imageInput = document.querySelector(inputSelector);
    const imageRow = document.querySelector(previewRowSelector);
    const addImageButton = document.querySelector(addButtonSelector);

    let selectedImages = [];

    // ✅ 1. 기존 이미지도 imageRow에 미리 렌더링
    if (window.modifyData?.attaches?.length > 0) {
        window.modifyData.attaches.forEach(attach => {
            const colDiv = document.createElement('div');
            colDiv.className = 'col-sm position-relative';

            colDiv.innerHTML = `
                <img src="${attach.attachPath}" class="img-fluid rounded shadow-sm" style="width:100%; height:auto;" />
                <button type="button" class="btn-close position-absolute top-0 end-0 m-1" aria-label="삭제"></button>
            `;

            // 삭제 시 hidden input 추가
            colDiv.querySelector('.btn-close').addEventListener('click', () => {
                imageRow.removeChild(colDiv);

                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'deleteImageNo';
                hiddenInput.value = attach.attachNo;

                imageRow.appendChild(hiddenInput);
            });

            imageRow.appendChild(colDiv);
        });
    }

    // ✅ 2. 새로운 이미지 업로드
    addImageButton.addEventListener('click', () => {
        imageInput.click();
    });

    imageInput.addEventListener('change', (event) => {
        const files = Array.from(event.target.files);

        if (selectedImages.length + files.length > maxCount) {
            alert(`이미지는 최대 ${maxCount}장까지만 업로드할 수 있습니다.`);
            return;
        }

        files.forEach((file) => {
            const reader = new FileReader();

            reader.onload = (e) => {
                const colDiv = document.createElement('div');
                colDiv.className = 'col-sm position-relative';

                colDiv.innerHTML = `
                    <img src="${e.target.result}" class="img-fluid rounded shadow-sm" style="width:100%; height:auto;" />
                    <button type="button" class="btn-close position-absolute top-0 end-0 m-1" aria-label="삭제"></button>
                `;

                colDiv.querySelector('.btn-close').addEventListener('click', () => {
                    imageRow.removeChild(colDiv);
                    selectedImages = selectedImages.filter(f => f !== file);
                });

                imageRow.appendChild(colDiv);
                selectedImages.push(file);
            };

            reader.readAsDataURL(file);
        });
    });

    return {
        resetImages: () => {
            imageRow.innerHTML = '';
            selectedImages = [];
            imageInput.value = '';
        }
    };
}