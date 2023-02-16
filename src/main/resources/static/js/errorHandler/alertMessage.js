

function catchErrorMessage(alertMessage){

    if ( !(alertMessage === null || alertMessage === '') ){
        Swal.fire({
            title: alertMessage,
            icon: 'error',
            confirmButtonText: '닫기'
        })
    }
}