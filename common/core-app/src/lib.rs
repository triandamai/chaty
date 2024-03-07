use std::sync::Arc;
mod hello;
use crate::hello::test::Test;



uniffi::setup_scaffolding!();

#[uniffi::export]
pub fn create_test_class(
    name: String
) -> Arc<Test> {
    Test::new(name)
}