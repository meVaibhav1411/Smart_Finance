document.querySelector('.c-form__next').addEventListener('click', function() {
  // Validate username field before moving on
  const usernameInput = document.querySelector('#step-email input');
  if (usernameInput.value.trim() === '') {
    alert('Please enter email');
    return;
  }

  document.getElementById('step-email').style.display = 'none';
  document.getElementById('step-password').style.display = 'block';
});
